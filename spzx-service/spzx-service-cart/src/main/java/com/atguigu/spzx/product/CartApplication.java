package com.atguigu.spzx.product;

import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.atguigu.spzx.product.feign")
@EnableH5Inter
@ComponentScan(basePackages = "com.atguigu")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class,args);
    }

}
