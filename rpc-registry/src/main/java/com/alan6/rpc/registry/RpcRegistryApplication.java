package com.alan6.rpc.registry;

import com.alan6.rpc.registry.config.RegistryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class RpcRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcRegistryApplication.class, args);
    }

}
