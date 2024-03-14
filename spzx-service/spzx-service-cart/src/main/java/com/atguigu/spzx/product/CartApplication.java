package com.atguigu.spzx.product;

import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.atguigu.spzx.product.feign") //feign接口所在的包
@EnableH5Inter //加载common-service的拦截器（商城端微服务专用的）
@ComponentScan(basePackages = "com.atguigu") //加载common-service下的异常处理器，swagger的配置类
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class,args);
    }

}
