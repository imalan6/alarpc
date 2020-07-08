package com.alan6.rpc.client.proxy;

import com.alan6.rpc.common.proxy.CoreInvocationHandler;

import java.lang.reflect.Method;

public class ClientInvocationHandler extends CoreInvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return super.invoke(proxy, method, args);
    }
}
