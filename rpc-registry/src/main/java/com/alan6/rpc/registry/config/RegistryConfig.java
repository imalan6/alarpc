package com.alan6.rpc.registry.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/6/22 17:33
 */

@Configuration
@Data
public class RegistryConfig {

    @Value("${alan6-rpc.registry.ip}")
    private String ZK_REGISTRY_IP;

    @Value("${alan6-rpc.registry.session-timeout:5000}")
    private int ZK_REGISTRY_SESSION_TIMEOUT;

    @Value("${alan6-rpc.registry.connect-timeout:1000}")
    private int ZK_REGISTRY_CONN_TIMEOUT;

    @Value("${alan6-rpc.registry.path:/registry}")
    private String ZK_REGISTRY_PATH;

    @Bean("ZK_REGISTRY_IP")
    public String registryIp(){
        return ZK_REGISTRY_IP;
    }

    @Bean("ZK_REGISTRY_SESSION_TIMEOUT")
    public int sessionTimeout(){
        return ZK_REGISTRY_SESSION_TIMEOUT;
    }

    @Bean("ZK_REGISTRY_CONN_TIMEOUT")
    public int connectTimeout(){
        return ZK_REGISTRY_CONN_TIMEOUT;
    }

    @Bean("ZK_REGISTRY_PATH")
    public String zkRegistryPath(){
        return ZK_REGISTRY_PATH;
    }
}
