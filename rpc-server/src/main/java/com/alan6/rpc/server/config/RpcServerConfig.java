package com.alan6.rpc.server.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/1 10:32
 */

@ConfigurationProperties(prefix = "alarpc.server")
public class RpcServerConfig {

    @Value("${port:8866}")
    private int tcpPort;

    @Value("${bosscount:5}")
    private int bossCount;

    @Value("${workercount:4}")
    private int workerCount;

    @Value("${keepalive:true}")
    private boolean keepAlive;

    @Value("${backlog:1000}")
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

    @Bean("ip")
    public String getIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    @Bean("port")
    public int getPort() throws UnknownHostException {
        return tcpPort;
    }
}
