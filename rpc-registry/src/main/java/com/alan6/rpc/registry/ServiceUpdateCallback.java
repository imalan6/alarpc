package com.alan6.rpc.registry;

import java.util.List;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/10 16:24
 */
public interface ServiceUpdateCallback {
    public void update(String path, List<String> serviceList);
}