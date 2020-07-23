package com.alan6.rpc.server.service;

import com.alan6.rpc.server.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService("goods")
public class GoodsImpl implements Goods {
    @Override
    public String getDetail(long id) {
        return "detail" + id;
    }
}
