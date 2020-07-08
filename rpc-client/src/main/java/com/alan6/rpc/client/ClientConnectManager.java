package com.alan6.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ClientConnectManager {
    private volatile static ClientConnectManager connectManage;

    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

    private Map<InetSocketAddress, RpcClientHandler> connectedServerNodes = new ConcurrentHashMap<>();


    public RpcClientHandler getRpcClientHandler() {


        return null;
    }

    private void connectServerNode(final InetSocketAddress remotePeer) {
        Bootstrap b = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());

        ChannelFuture channelFuture = b.connect(remotePeer);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    log.debug("Successfully connect to remote server. remote peer = " + remotePeer);
                    RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                }
            }
        });
    }
}
