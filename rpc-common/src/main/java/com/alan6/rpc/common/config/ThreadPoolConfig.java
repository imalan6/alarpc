package com.alan6.rpc.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/8 10:12
 */

@ConfigurationProperties(prefix = "alarpc.threadpool")
@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Value("${coreSize:10}")
    private int corePoolSize;

    @Value("${maxSize:50}")
    private int maxPoolSize;

    @Value("${queueCapacity:200}")
    private int queueCapacity;

    @Value("${keepAlive:60}")
    private int keepAlive;

    @Value("${waitTime:60}")
    private int AwaitTerminationSeconds;

    @Value("${namePrefix:alarpc-executor-}")
    private String namePrefix;

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 设置核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);

        // 设置最大线程数
        taskExecutor.setMaxPoolSize(maxPoolSize);

        // 设置队列容量
        taskExecutor.setQueueCapacity(queueCapacity);

        // 设置线程活跃时间（秒）
        taskExecutor.setKeepAliveSeconds(keepAlive);

        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix(namePrefix);

        // 设置拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务结束后再关闭线程池
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        // 设置等待终止时间
        taskExecutor.setAwaitTerminationSeconds(AwaitTerminationSeconds);

//        taskExecutor.initialize();

        return taskExecutor;
    }
}
