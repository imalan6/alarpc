package com.alan6.rpc.client.starter;

import com.alan6.rpc.client.ClientConnectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/10 16:39
 */

@Component
public class ServiceDiscoverStarter implements ApplicationRunner {

    @Autowired
    private ClientConnectManager connectManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        connectManager.connectRegistry();
    }
}
