package com.atguigu.spzx.order;

import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableH5Inter
@ComponentScan(basePackages = "com.atguigu")
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }

}
