package com.alan6.rpc.server.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/1 10:32
 */

@Configuration
public class RpcServerConfig {

    @Value("${alan6-rpc.server.port:8866}")
    private int tcpPort;

    @Value("${alan6-rpc.server.bossthread.count:5}")
    private int bossCount;

    @Value("${alan6-rpc.server.workerthread.count:4}")
    private int workerCount;

    @Value("${alan6-rpc.server.so.keepalive:true}")
    private boolean keepAlive;

    @Value("${alan6-rpc.server.so.backlog:1000}")
    private int backlog;

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return  new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean("inetSocketAddress")
    public InetSocketAddress inetSocketAddress() {
        return new InetSocketAddress(tcpPort);
    }
}
