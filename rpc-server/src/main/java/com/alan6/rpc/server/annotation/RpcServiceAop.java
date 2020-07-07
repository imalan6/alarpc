package com.alan6.rpc.server.annotation;

import com.alan6.rpc.common.util.StringUtil;
import com.alan6.rpc.registry.ServiceRegistry;
import com.alan6.rpc.registry.zookeeper.ZkConnectManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
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
public class RpcServiceAop implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private InetSocketAddress inetSocketAddress;

    @Resource(name = "zkConnectManager")
    private ServiceRegistry serviceRegistry;

    private Map<String, Object> serviceBeanMap = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 扫描带有 RpcService 注解的类并初始化 handlerMap 对象
        Map<String, Object> serviceBeanMap = event.getApplicationContext().getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value();
                String serviceVersion = rpcService.version();
                if (StringUtil.isNotEmpty(serviceVersion)) {
                    serviceName += ":" + serviceVersion;
                }
                serviceBeanMap.put(serviceName, serviceBean);
                log.info("Rpc service:【{}】 loaded", serviceName);
            }
        }


        // 注册 RPC 服务地址
        new Thread(new Runnable() {
            CountDownLatch latch = new CountDownLatch(1);

            @Override
            public void run() {
                ServiceRegistry serviceRegistry = null;
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

                log.info("Starting register rpc service...");

                if (serviceBeanMap.size() == 0){
                    log.info("No rpc services need to register");
                }else {
                    for (String interfaceName : serviceBeanMap.keySet()) {
                        try {
                            serviceRegistry.register(interfaceName, inetSocketAddress.getAddress().getHostAddress());
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        log.info("Register rpc service success: 【{}】 => 【{}】", interfaceName, inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort());
                    }
                }
            }
        }).start();

        log.info("Waiting for connecting zookeeper server...");
    }

    public Map<String, Object> getServiceBeanMap() {
        return this.serviceBeanMap;
    }
}
