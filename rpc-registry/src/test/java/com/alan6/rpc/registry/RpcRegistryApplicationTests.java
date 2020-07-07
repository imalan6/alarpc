package com.alan6.rpc.registry;

import com.alan6.rpc.registry.config.RegistryConfig;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RpcRegistryApplicationTests {

    @Autowired
    private RegistryConfig config;

    @Test
    void contextLoads() {
        log.info("IP:{}", config.getRegistryIp());
    }

}
