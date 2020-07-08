package com.alan6.rpc.registry.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/6/22 17:33
 */

@ConfigurationProperties(prefix = "alarpc.registry")
@Data
public class RegistryConfig {

    @Value("${ip}")
    private String registryIp;

    @Value("${session-timeout:5000}")
    private int sessionTimeOut;

    @Value("${connect-timeout:1000}")
    private int connTimeOut;

    @Value("${path:/registry}")
    private String registryPath;

    @Bean("registryIp")
    public String registryIp(){
        return registryIp;
    }

    @Bean("sessionTimeOut")
    public int sessionTimeOut(){
        return sessionTimeOut;
    }

    @Bean("connTimeOut")
    public int connTimeOut(){
        return connTimeOut;
    }

    @Bean("registryPath")
    public String registryPath(){
        return registryPath;
    }
}
