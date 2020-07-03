package com.alan6.rpc.registry;

import org.apache.zookeeper.ZooKeeper;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/3 13:47
 */
public interface ConnectManager {

    public ZooKeeper connectServer(String ip, int timeout);

    public void watchNode(final ZooKeeper zk, String path);

    public void stop();
}
