package com.atguigu.spzx.common.feigninter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = FeignInterceptor.class)            // 通过Import注解导入日志切面类到Spring容器中
public @interface EnableFeignInterceptor {
    
}