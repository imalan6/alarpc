package com.alan6.rpc.registry;

import org.apache.zookeeper.KeeperException;

import java.util.List;

/**
 * @author： Alan6
 * @Description：
 * @date： 2020/6/22 17:15
 */
public interface ServiceDiscovery extends BaseConnectManager {

    /**
     * @Description: 服务发现
     * @Params: serviceName 服务名称
     * @Return: 服务地址
     */
    public List<String> discover(String serviceName) throws KeeperException, InterruptedException;

    public void watchNode(String path);
}
