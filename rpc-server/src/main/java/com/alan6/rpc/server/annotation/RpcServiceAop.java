package com.alan6.rpc.server.annotation;

import com.alan6.rpc.common.util.StringUtil;
import com.alan6.rpc.registry.ServiceRegistry;
import com.alan6.rpc.server.cache.RpcServerCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/1 14:11
 */

@Slf4j
@Component
public class RpcServiceAop implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private InetSocketAddress inetSocketAddress;

    @Autowired
    private ServiceRegistry serviceRegistry;

    private Map<String, Object> serviceBeanMap = RpcServerCache.getServiceBeanMap();

    @Autowired
    private AsynTask task;

    @Autowired
    private String ip;

    @Autowired
    private int port;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 扫描带有 RpcService 注解的类并初始化 handlerMap 对象
        Map<String, Object> beanMap = event.getApplicationContext().getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(beanMap)) {
            for (Object serviceBean : beanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value();
                String serviceVersion = rpcService.version();
                if (StringUtil.isNotEmpty(serviceVersion)) {
                    serviceName += ":" + serviceVersion;
                }
                serviceBeanMap.put(serviceName, serviceBean);
                log.info("Load Rpc service:【{}】", serviceName);
            }
        }

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
                    serviceRegistry.connectRegistry(new Watcher() {
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

                log.info("========== Starting register rpc service ===========");

                //注册服务
                this.registerService();
            }
        }

        // 注册服务
        public void registerService() {
            if (serviceBeanMap.size() == 0) {
                log.info("No rpc service need to register");
            } else {
                try {
                    serviceRegistry.register(serviceBeanMap, ip + ":" + port);
                } catch (KeeperException | InterruptedException e) {
                    log.error("Register rpc service error");
                }
            }
        }
    }
}
