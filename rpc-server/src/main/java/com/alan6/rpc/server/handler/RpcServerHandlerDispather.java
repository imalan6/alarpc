package com.alan6.rpc.server.handler;

import com.alan6.rpc.common.RpcRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@ChannelHandler.Sharable
@Component
public class RpcServerHandlerDispather extends AbstractRpcServerHandler {

    @Override
    protected void doRequest(ChannelHandlerContext ctx, RpcRequest request){
        log.debug("******************进入doRequest********************");
        Channel channel = ctx.channel();

    }
}
