package com.alan6.rpc.server;

import com.alan6.rpc.server.service.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RpcServerApplicationTests {

    @Autowired
    private User user;

    @Test
    void contextLoads() {
        log.info(user.getUserName(1000));
    }

}
