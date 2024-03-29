package com.atguigu.spzx.user;

import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableH5Inter
@ComponentScan(basePackages = "com.atguigu")
@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
