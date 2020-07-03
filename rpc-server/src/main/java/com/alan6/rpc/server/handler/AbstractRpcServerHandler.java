package com.alan6.rpc.server.handler;

import com.alan6.rpc.common.RpcRequest;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@Slf4j
public abstract class AbstractRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    protected  abstract void doRequest(ChannelHandlerContext ctx, RpcRequest request);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        log.debug("收到客户端【{}】请求，request=【{}】", request);
        Optional.ofNullable(request)
                .ifPresent( t -> doRequest(ctx, request));
    }

    //链接关闭
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
    }
}
