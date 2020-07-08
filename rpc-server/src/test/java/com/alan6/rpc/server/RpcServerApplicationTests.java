package com.alan6.rpc.server;

import com.alan6.rpc.server.service.TestUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RpcServerApplicationTests {

    @Autowired
    private TestUser user;

    @Test
    void contextLoads() {
        log.info(user.getUserName(1000));
    }

}
