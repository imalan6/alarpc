package com.alan6.rpc.registry.zookeeper;

import com.alan6.rpc.registry.BaseRegistry;
import com.alan6.rpc.registry.Constant;
import com.alan6.rpc.registry.ServiceDiscovery;
import com.alan6.rpc.registry.config.RegistryConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 基于 ZooKeeper 的服务发现接口实现
 *
 * @author huangyong
 * @since 1.0.0
 */
@Slf4j
@Component
public class ZkServiceDiscovery extends BaseRegistry implements ServiceDiscovery {

    @Resource
    private RegistryConfig config;

    @Resource
    private ZkConnectManager zkConnectManager;

    private ZooKeeper zk;

    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public List<String> discover(String serviceName) throws KeeperException, InterruptedException {

        if (zk != null) {
            String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;

            Stat exist = zk.exists(servicePath, false);
            if (exist == null) {
                log.error("");
            }

            // 获取服务地址列表
            List<String> addressList = zk.getChildren(servicePath, null);
            if (addressList != null && addressList.size() > 0) {
                return addressList;
            }
        }
        return null;
    }
}