package com.alan6.rpc.server.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author: Alan6
 * @Description:
 * @date: 2020/7/3 15:45
 */

@Aspect
@Component
@Slf4j
public class RpcServiceAnnoAop {

    @Pointcut("@annotation(com.alan6.rpc.server.annotation.RpcService)")
    private void pointcut() {
        log.info("pointcut");
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        //类名
        String clsName=joinPoint.getSignature().getDeclaringType().getSimpleName();
        //方法名
        String modName= joinPoint.getSignature().getName();
        //参数
        Object[] args = joinPoint.getArgs();
        StringBuffer result = new StringBuffer();
        result.append("["+clsName+"]");
        result.append("["+modName+"]");
        log.info(result.toString());
    }

    @Around("pointcut()")
    public void around(JoinPoint joinPoint) {

        log.info("around");
    }

    // 后置通知
    @After("pointcut()")
    public void after() {
        log.info("====后置通知start");

        log.info("====后置通知end");
    }
}
