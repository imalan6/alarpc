package com.alan6.rpc.server.service;

import com.alan6.rpc.server.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/3 17:24
 */

@Service
@RpcService("user")
public class TestUser implements User {
    @Override
    public String getUserName(long id) {
        return "zhangsan";
    }
}
