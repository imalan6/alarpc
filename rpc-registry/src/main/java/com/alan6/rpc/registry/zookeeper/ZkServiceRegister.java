package com.alan6.rpc.registry.zookeeper;

import com.alan6.rpc.registry.BaseRegistry;
import com.alan6.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.stereotype.Component;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/1 14:23
 */
@Slf4j
public class ZkServiceRegister extends BaseRegistry implements ServiceRegistry {

    private ZkClient zkClient;

    @Override
    public void register(String serviceName, String serviceAddress) {
        // 创建 registry 节点（持久）
        if (!zkClient.exists(ZK_REGISTRY_PATH)) {
            zkClient.createPersistent(ZK_REGISTRY_PATH);
            log.debug("create registry node: {}", ZK_REGISTRY_PATH);
        }
        // 创建 service 节点（持久）
        String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            log.debug("create service node: {}", servicePath);
        }
        // 创建 address 节点（临时）
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        log.debug("create address node: {}", addressNode);
    }
}
