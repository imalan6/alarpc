package com.alan6.rpc.client.annotation;

import com.alan6.rpc.client.cache.RpcCacheManager;
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

    @Autowired
    private RpcCacheManager cacheManager;

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
        try {
            task.connectRegistry();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Connecting registry server...");
    }

    @Component
    class AsynTask {

        @Async("taskExecutor")
        public void connectRegistry() throws Exception {
            CountDownLatch latch = new CountDownLatch(1);
            log.debug("thread name: {}", Thread.currentThread().getName());

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
                    if (serviceList != null && serviceList.size() > 0) {
                        log.info("Rpc service list:【{}】", serviceList);
                    } else {
                        log.info("No rpc service found:【{}】", path);
                    }
                }

                @Override
                public void update(String service, List<String> nodeList) throws Exception {
                    if (nodeList != null && nodeList.size() > 0) {
                        log.info("Rpc service update:【{}】=>【{}】", service, nodeList);
                        cacheManager.set(service, nodeList);
                    } else {
                        log.info("Rpc service offline:【{}】", service);
                    }
                }
            });
        }
    }
}
