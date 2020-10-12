package com.alan6.rpc.client;

import com.alan6.rpc.registry.ServiceDiscovery;
import com.alan6.rpc.registry.ServiceUpdateCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class ClientConnectManager {

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Autowired
    private String registryPath;

    private Map<InetSocketAddress, RpcClientHandler> connectedServerNodes = new ConcurrentHashMap<>();

    public RpcClientHandler getRpcClientHandler(){
        return new RpcClientHandler();
    }

    @Async("taskExecutor")
    public void connectRegistry() {
        {
            CountDownLatch latch = new CountDownLatch(1);

            try {
                serviceDiscovery.connectRegistry(new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            log.info("Connect zookeeper server successful!");
                            latch.countDown();
                        }
                    }
                });

                latch.await();
            } catch (InterruptedException e) {
                log.error("{}", e.toString());
            }

            log.info("========== Starting get rpc service list ===========");

            this.getRpcService();
        }
    }

    // 发现、更新服务
    public void getRpcService() {
        try {
            serviceDiscovery.watchNode("/registry", new ServiceUpdateCallback() {
                @Override
                public void get(String path, List<String> serviceList) {
                    if (serviceList != null && serviceList.size() > 0) {
                        log.info("【Get】Rpc service:{}, address list：{}", path, serviceList);
                    }
                }

                @Override
                public void update(String path, List<String> serviceList) {
                    if (serviceList != null && serviceList.size() > 0) {
                        log.info("【Update】Rpc service:{}, address list：{}", path, serviceList);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
