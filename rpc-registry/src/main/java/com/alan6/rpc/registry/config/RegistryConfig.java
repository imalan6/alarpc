package com.alan6.rpc.registry.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/6/22 17:33
 */

@Configuration
@Data
public class RegistryConfig {

    @Value("${alan6-rpc.registry.ip:127.0.0.1}")
    private String registryIp;

    @Value("${alan6-rpc.registry.session-timeout:5000}")
    private int sessionTimeOut;

    @Value("${alan6-rpc.registry.connect-timeout:1000}")
    private int connTimeOut;

    @Value("${alan6-rpc.registry.path:/registry}")
    private String registryPath;
}
