package com.atguigu.spzx.common.h5inter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = {H5UserInterceptor.class,H5Configuration.class})
public @interface EnableH5Inter {
    
}