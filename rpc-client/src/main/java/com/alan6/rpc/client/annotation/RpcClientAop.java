package com.alan6.rpc.client.annotation;

import com.alan6.rpc.registry.ServiceDiscovery;
import com.alan6.rpc.registry.ServiceUpdateCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/1 14:11
 */

@Slf4j
@Component
public class RpcClientAop implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    private Map<String, Object> serviceBeanMap = new ConcurrentHashMap<>();

    @Autowired
    private AsynTask task;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 扫描带有 RpcClient 注解的类并初始化 handlerMap 对象
/*        Map<String, Object> serviceBeanMap = event.getApplicationContext().getBeansWithAnnotation(RpcClient.class);

        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcClient rpcClient = serviceBean.getClass().getAnnotation(RpcClient.class);
                String serviceName = serviceBean.getClass().getPackage().getName() + "." + rpcClient.value();
                String serviceVersion = rpcClient.version();
                if (StringUtil.isNotEmpty(serviceVersion)) {
                    serviceName += ":" + serviceVersion;
                }
                log.info("Rpc service loaded:【{}】", serviceName);
                serviceBeanMap.put(serviceName, serviceBean);
                log.info("Rpc service:【{}】 loaded", serviceName);
            }
        }*/

        // 连接服务注册中心
        task.connectRegistry();

        log.info("Connecting registry server...");
    }

    public Map<String, Object> getServiceBeanMap() {
        return this.serviceBeanMap;
    }

    @Component
    class AsynTask {

        @Async("taskExecutor")
        public void connectRegistry() {
            {
                CountDownLatch latch = new CountDownLatch(1);
                log.debug("【debug】{}", Thread.currentThread().getName());

                try {
                    serviceDiscovery.connectRegistry(new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            if (event.getState() == Event.KeeperState.SyncConnected) {
                                log.info("Connect registry server success!");
                                latch.countDown();
                            }
                        }
                    });
                    latch.await();
                } catch (InterruptedException e) {
                    log.error("Connect registry server fail");
                }

                log.info("========== Starting discover rpc service ===========");

                //发现服务
                serviceDiscovery.watchNode("/registry", new ServiceUpdateCallback() {
                    @Override
                    public void get(String path, List<String> serviceList) {
                        log.info("Get rpc service list: {}", serviceList);
                    }

                    @Override
                    public void update(String path, List<String> serviceList) {
                        log.info("Update rpc service list: {}", serviceList);
                    }
                });
            }
        }
    }
}
