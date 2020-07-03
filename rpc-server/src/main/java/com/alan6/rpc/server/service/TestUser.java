package com.alan6.rpc.server.service;

import org.springframework.stereotype.Service;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/3 17:24
 */

@Service
public class TestUser implements User {
    @Override
    public String getUserName(long id) {
        return "zhangsan";
    }
}
