package com.alan6.rpc.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RpcCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcCommonApplication.class, args);
    }

}
