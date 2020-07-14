package com.alan6.rpc.server.cache;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/13 14:30
 */

@Data
public class RpcServerCache {

    private static final Map<String, Object> serviceBeanMap = new ConcurrentHashMap<>();

    public static Map<String, Object> getServiceBeanMap(){
        return serviceBeanMap;
    }

    public static void setServiceBeanMap(Map<String, Object> serviceBeanMap){
        RpcServerCache.serviceBeanMap.putAll(serviceBeanMap);
    }
}
