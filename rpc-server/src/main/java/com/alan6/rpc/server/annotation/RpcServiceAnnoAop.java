package com.alan6.rpc.server.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

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
}
