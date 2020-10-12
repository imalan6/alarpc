package com.alan6.rpc.client.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/13 16:13
 */
@Component
@Slf4j
@Data
public class RpcCacheManager {

    @Autowired
    @Lazy
    private CacheManager cacheManager;

    private static final String CACHE_NAME = "serviceCache";

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    public void set(String key, Object value, int timeToLiveSeconds){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Element element = new Element(
                key,
                value,
                0,
                timeToLiveSeconds);
        cache.put(element);
        log.debug("add in cache service:{}, node list{}", key, value);
    }

    /**
     * 添加缓存
     * 使用默认生存时间
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Element element = new Element(
                key, value,
                0,
                12 * 60 * 60);
        cache.put(element);
        log.debug("add in cache, service:{}, node list{}", key, value);
    }

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param timeToIdleSeconds 对象空闲时间，指对象在多长时间没有被访问就会失效。
     *                          只对eternal为false的有效。传入0，表示一直可以访问。以秒为单位。
     * @param timeToLiveSeconds 缓存生存时间（秒）
     *                          只对eternal为false的有效
     */
    public void set(String key, Object value, int timeToIdleSeconds, int timeToLiveSeconds){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Element element = new Element(
                key, value,
                timeToIdleSeconds,
                timeToLiveSeconds);
        cache.put(element);
        log.debug("add in cache, service:{}, node list{}", key, value);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public Object get(String key){
        Cache cache = cacheManager.getCache(CACHE_NAME);
        Element element = cache.get(key);
        if(element == null){
            return null;
        }
        log.debug("get cache element：{}", element);
        return element.getObjectValue();
    }
}
