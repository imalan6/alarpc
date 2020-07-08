package com.alan6.rpc.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/1 14:13
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
    /**
     * 服务接口类
     */
    String value();

    /**
     * 服务版本号
     */
    String version() default "";
}
