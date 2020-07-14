package com.alan6.rpc.client;

import com.alan6.rpc.client.service.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RpcClientApplication.class)
@Slf4j
class RpcClientApplicationTests {

    @Autowired
    private ClientConnectManager clientConnectManager;

    @Test
    void contextLoads() {
        User user = RpcClient.create(User.class);
        log.info(user.getName(1000));
    }
}
