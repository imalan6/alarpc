package com.alan6.rpc.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/14 9:58
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClient {
    /**
     * 服务接口类
     */
    String value();

    /**
     * 服务版本号
     */
    String version() default "";
}
