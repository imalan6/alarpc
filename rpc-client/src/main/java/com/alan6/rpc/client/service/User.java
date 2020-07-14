package com.alan6.rpc.client.service;

import com.alan6.rpc.client.annotation.RpcClient;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/13 16:04
 */

@RpcClient("user")
public interface User {
    public String getName(long id);
}
