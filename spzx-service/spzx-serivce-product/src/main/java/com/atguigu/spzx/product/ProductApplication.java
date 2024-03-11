package com.atguigu.spzx.product;

import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableH5Inter
@EnableCaching
@ComponentScan(basePackages = "com.atguigu")
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class,args);
    }
}
