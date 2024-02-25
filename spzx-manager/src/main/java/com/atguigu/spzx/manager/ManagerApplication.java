package com.atguigu.spzx.manager;

import com.atguigu.spzx.manager.auth.UserAuthProperties;
import com.atguigu.spzx.manager.properties.SpzxMinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.atguigu")
@SpringBootApplication
@EnableConfigurationProperties(value = {UserAuthProperties.class, SpzxMinioProperties.class})
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
