package com.alan6.rpc.server.service;

import com.alan6.rpc.server.annotation.RpcService;
import org.springframework.stereotype.Service;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/7 17:52
 */

@Service
@RpcService("user")
public class UserImpl implements User {

    @Override
    public String getUserName(long id) {
        return "zhangsan" + id;
    }
}
