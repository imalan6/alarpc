package com.alan6.rpc.registry;

import org.apache.zookeeper.Watcher;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/7 17:58
 */
public interface BaseConnectManager {

    public void connectRegistry(Watcher watcher);

    public void stop();
}
