package com.alan6.rpc.client.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EhCacheConfig {

    /**
     * 获取缓存管理器
     *
     * @return
     */
    @Bean
    public CacheManager getCacheManager() {
        // 本项目的cache
        CacheConfiguration config = new CacheConfiguration();
        config.setName("serviceCache");
        config.setEternal(false);//是否永不过期，为false则过期需要通过timeToIdleSeconds，timeToLiveSeconds判断
        config.setMemoryStoreEvictionPolicy("LFU");//最少使用
        config.setMaxElementsInMemory(1000);//内存中存放的最大记录数
        config.setMaxElementsOnDisk(20000);
        config.setOverflowToDisk(true);//内存中过多则存入硬盘
        config.setDiskPersistent(false);//重启服务后是否恢复缓存

        // 设置ehcache配置文件，获取CacheManager
        Configuration configuration = new Configuration();
        configuration.addCache(config);
        CacheManager cacheManager = CacheManager.newInstance(configuration);

        // 将CacheManager注册为bean，供缓存工具类使用
        return cacheManager;
    }
}
