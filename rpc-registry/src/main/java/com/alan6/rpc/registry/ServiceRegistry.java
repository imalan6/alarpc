package com.alan6.rpc.registry;

import org.apache.zookeeper.KeeperException;

import java.util.Map;

/**
 * @author： Alan6
 * @Description：
 * @date： 2020/6/22 17:30
 */
public interface ServiceRegistry extends BaseConnectManager {
    /**
     * 注册服务
     *
     * @param serviceBeanMap    服务bean Map
     * @param serviceAddress 服务地址
     */
    void register(Map<String, Object> serviceBeanMap, String serviceAddress) throws KeeperException, InterruptedException;
}
