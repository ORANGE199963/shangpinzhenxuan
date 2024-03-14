package com.atguigu.spzx.order;

import com.atguigu.spzx.common.feigninter.EnableFeignInterceptor;
import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignInterceptor //注解标注在订单服务，当订单服务通过feign发起请求去调用目标服务时，会先执行FeignInterceptor拦截器的apply方法
@EnableFeignClients(basePackages = {
        "com.atguigu.spzx.cart.feign",
        "com.atguigu.spzx.user.feign",
        "com.atguigu.spzx.product.feign",
        "com.atguigu.spzx.pay.feign"
})
@EnableH5Inter //h5端微服务的拦截器（从redis中取出来的，共享threadLocal）
@ComponentScan(basePackages = "com.atguigu") //异常处理器，swagger文档配置类
//@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

}
