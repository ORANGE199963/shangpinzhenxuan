package com.atguigu.spzx.pay;


import com.atguigu.spzx.common.feigninter.EnableFeignInterceptor;
import com.atguigu.spzx.common.h5inter.EnableH5Inter;
import com.atguigu.spzx.pay.properties.AlipayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@EnableFeignClients(basePackages = {
        "com.atguigu.spzx.order.feign",
        "com.atguigu.spzx.product.feign"
})
@EnableConfigurationProperties(value = {AlipayProperties.class})
@EnableFeignInterceptor //当支付服务去调用其它微服务时，通过feign发起http请求，会将上一段请求的token放在feign的请求中的
@EnableH5Inter //微服务的拦截器（根据请求头token从redis中获取用户信息，进行线程共享）
@ComponentScan(basePackages = "com.atguigu")
@SpringBootApplication
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }
}
