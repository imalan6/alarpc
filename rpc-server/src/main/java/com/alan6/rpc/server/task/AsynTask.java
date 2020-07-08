package com.alan6.rpc.server.task;

import com.alan6.rpc.registry.ServiceRegistry;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/8 17:39
 */
@Component
@Slf4j
public class AsynTask {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public void connectRegistry(ServiceRegistry serviceRegistry, Map<String, Object> serviceBeanMap, InetSocketAddress inetSocketAddress) {
        {
            log.debug("【debug】Current thread name:{}", Thread.currentThread().getName());
            log.debug("【debug】{}", taskExecutor.getThreadNamePrefix());
            log.debug("【debug】{}", taskExecutor.getCorePoolSize());
            log.debug("【debug】{}", taskExecutor.getMaxPoolSize());
            log.debug("【debug】{}", taskExecutor.getKeepAliveSeconds());
            log.debug("【debug】{}", taskExecutor.getActiveCount());

            CountDownLatch latch = new CountDownLatch(1);

            try {
                serviceRegistry.connectRegistry(new Watcher() {
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

            log.info("========== Starting register rpc service ===========");

            this.registerService(serviceRegistry, serviceBeanMap, inetSocketAddress);
        }
    }

    // 注册服务
    public void registerService(ServiceRegistry serviceRegistry, Map<String, Object> serviceBeanMap, InetSocketAddress inetSocketAddress) {
        if (serviceBeanMap.size() == 0) {
            log.info("No rpc services need to register");
        } else {
            for (String interfaceName : serviceBeanMap.keySet()) {
                try {
                    serviceRegistry.register(interfaceName, inetSocketAddress.getAddress().getHostAddress());
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("【Register】Register rpc service success: 【{}】 => 【{}】", interfaceName, inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort());
            }
        }
    }
}