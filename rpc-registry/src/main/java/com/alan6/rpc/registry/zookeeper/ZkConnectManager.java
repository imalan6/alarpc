package com.alan6.rpc.registry.zookeeper;

import com.alan6.rpc.registry.ServiceDiscovery;
import com.alan6.rpc.registry.ServiceRegistry;
import com.alan6.rpc.registry.config.RegistryConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/6/24 10:06
 */
@Slf4j
@Component("zkConnectManager")
public class ZkConnectManager implements ServiceRegistry, ServiceDiscovery {

    @Autowired
    private String registryIp;

    @Autowired
    private int connTimeOut;

    @Autowired
    private String registryPath;

    private ZooKeeper zookeeper;

    @Override
    public void connectRegistry(Watcher watcher){
        if (registryIp == null){
            log.error("Please config registry server ip!!!");
            return;
        }

        if (zookeeper == null){
            zookeeper = this.connectServer(registryIp, connTimeOut, watcher);
        }
    }

    public ZooKeeper connectServer(String ip, int timeout, Watcher watcher) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(ip, timeout, watcher);
        } catch (Exception e) {
            log.error("Connect zookeeper server error {}", e);
        }
        return zk;
    }

    public ZooKeeper connectServer(String ip, int timeout) {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(ip, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
            log.info("Connected zookeeper server");
        } catch (IOException | InterruptedException e) {
            log.error("Connect zookeeper server error {}", e);
        }
        return zk;
    }

    @Override
    public void watchNode(String path) {
        if (zookeeper != null){
            try {
                List<String> dataList = zookeeper.getChildren(path, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getType() == Event.EventType.NodeChildrenChanged) {
                            watchNode(path);
                        }
                    }
                });

                // registry目录
                if (path.equals(registryPath)) {
                    for (String serviceName : dataList) {
                        String servicePath = registryPath + "/" + serviceName;
                        // 服务递归watch
                        watchNode(servicePath);
                    }
                    log.debug("services:{}", dataList);
                } else { // 服务目录
                    log.debug("service:{}, addresses: {}", path, dataList);
                }
            } catch (KeeperException | InterruptedException e) {
                log.error("", e);
            }
        }

    }

    @Override
    public void register(String serviceName, String serviceAddress) throws KeeperException, InterruptedException {
        if (zookeeper != null){
            // 创建 registry 节点（持久）
            if (zookeeper.exists(registryPath, false) == null) {
                zookeeper.create(registryPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.debug("create registry node: {}", registryPath);
            }

            // 创建 service 节点（持久）
            String servicePath = registryPath + "/" + serviceName;
            if (zookeeper.exists(servicePath, false) == null) {
                zookeeper.create(servicePath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.debug("create service node: {}", servicePath);
            }

            // 创建 address 节点（临时）
            String addressPath = servicePath + "/" + serviceAddress;
            if (zookeeper.exists(addressPath, false) == null) {
                zookeeper.create(addressPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                log.debug("create address node: {}", servicePath);
            }
            log.debug("serviceAddress has been registered: {}", addressPath);
        }
    }

    @Override
    public List<String> discover(String serviceName) throws KeeperException, InterruptedException {
        List<String> addressList = null;

        if (zookeeper != null) {
            String servicePath = registryPath + "/" + serviceName;

            Stat exist = zookeeper.exists(servicePath, false);
            if (exist == null) {
                log.error("Cannot find rpc service:【{}】 ", serviceName);
                return null;
            }

            // 获取服务地址列表
            addressList = zookeeper.getChildren(servicePath, null);
        }
        return addressList;
    }

    @Override
    public void stop() {
        if (zookeeper != null) {
            try {
                zookeeper.close();
            } catch (InterruptedException e) {
                log.error("Close zookeeper client error {}", e);
            }
        }
    }
}
