package com.alan6.rpc.registry.zookeeper;

import com.alan6.rpc.registry.ServiceDiscovery;
import com.alan6.rpc.registry.ServiceRegistry;
import com.alan6.rpc.registry.ServiceUpdateCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/6/24 10:06
 */
@Slf4j
@Component
public class ZkConnectManager implements ServiceDiscovery, ServiceRegistry {

    @Autowired
    private String registryIp;

    @Autowired
    private int connTimeOut;

    @Autowired
    private String registryPath;

    private static ZooKeeper zookeeper;

    @Override
    public void connectRegistry(Watcher watcher) {
        if (registryIp == null) {
            log.error("Please config registry server ip!!!");
            return;
        }

        if (zookeeper == null) {
            zookeeper = this.connectServer(registryIp, connTimeOut, watcher);
        }
    }

    public ZooKeeper connectServer(String ip, int timeout, Watcher watcher) {
        ZooKeeper zk = null;
        try {
            log.info("Starting connect zookeeper server.");
            zk = new ZooKeeper(ip, timeout, watcher);
        } catch (Exception e) {
            log.error("Connect zookeeper server error {}", e);
        }
        return zk;
    }

    @Override
    public void watchNode(String path, ServiceUpdateCallback callback) throws Exception {
        if (zookeeper != null) {
            try {
                List<String> dataList = zookeeper.getChildren(path, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getType() == Event.EventType.NodeChildrenChanged) {
                            try {
                                watchNode(path, callback);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                // registry目录
                if (path.equals(registryPath)) {

                    callback.get(path, dataList);

                    for (String serviceName : dataList) {
                        String servicePath = registryPath + "/" + serviceName;
                        // 服务递归watch
                        watchNode(servicePath, callback);
                    }
                } else { // 服务目录
                    callback.update(path, dataList);
                }
            } catch (KeeperException | InterruptedException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 列出指定path下所有节点
     */
    public void lsr(String path) throws Exception {
        if (zookeeper == null){
            return;
        }
        List<String> list = zookeeper.getChildren(path,null);
        //判断是否有子节点
        if(list.isEmpty() || list == null){
            return;
        }

        for(String s : list){
            //判断是否为根目录
            if(path.equals("/")){
                lsr(path + s);
            }else {
                log.info("{}", path +"/" + s);
                lsr(path +"/" + s);
            }
        }
    }

    @Override
    public void register(Map<String, Object> serviceBeanMap, String serviceAddress) throws KeeperException, InterruptedException {
        if (zookeeper != null) {
            // 创建 registry 节点（持久）
            if (zookeeper.exists(registryPath, false) == null) {
                zookeeper.create(registryPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info("Create registry node: {}", registryPath);
            } else {
                log.info("Found registry node: {}", registryPath);
            }

            for (String service : serviceBeanMap.keySet()) {
                // 创建 service 节点（持久）
                String servicePath = registryPath + "/" + service;
                if (zookeeper.exists(servicePath, false) == null) {
                    zookeeper.create(servicePath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    log.info("Create service node: {}", servicePath);
                }else {
                    log.info("Found service node: {}", servicePath);
                }

                // 创建 address 节点（临时）
                String addressPath = servicePath + "/" + serviceAddress;
                if (zookeeper.exists(addressPath, false) == null) {
                    zookeeper.create(addressPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    log.info("Create address node: {}", addressPath);
                }else {
                   log.info("Found address node: {}", addressPath);
                }
                log.info("【Register】Register rpc service success: 【{}】 => 【{}】", service, serviceAddress);
                log.info("-----------------------------------------");
            }
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
