package com.alan6.rpc.registry;

import org.apache.zookeeper.KeeperException;

/**
 * @author： Alan6
 * @Description：
 * @date： 2020/6/22 17:30
 */
public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param serviceName    服务名称
     * @param serviceAddress 服务地址
     */
    void register(String serviceName, String serviceAddress) throws KeeperException, InterruptedException;
}
