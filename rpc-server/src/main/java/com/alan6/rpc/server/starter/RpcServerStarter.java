package com.alan6.rpc.server.starter;

import com.alan6.rpc.server.annotation.RpcServiceAop;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;


@Component
@Slf4j
public class RpcServerStarter {

    @Autowired
    private NioEventLoopGroup bossGroup;

    @Autowired
    private NioEventLoopGroup workerGroup;

    @Autowired
    private InetSocketAddress inetSocketAddress;

    @Autowired
    private ServerChannelInitializer serverChannelInitializer;

    @Autowired
    private RpcServiceAop serviceParse;

    @PostConstruct
    public void start() throws InterruptedException, KeeperException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)

                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .localAddress(inetSocketAddress)
                .childHandler(serverChannelInitializer);
        ChannelFuture future = bootstrap.bind().sync();
        if(future.isSuccess()) {
            log.info("启动rpc Server服务");
        }
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        log.info("关闭rpc server服务");
    }
}
