package com.atguigu.spzx.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.atguigu.spzx.pay.properties.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @Autowired
    AlipayProperties alipayProperties;


    @Bean
    public AlipayClient alipayClient(){
        //无论调用支付宝的那个接口，都是要使用到一个AlipayClient对象（单例即可）
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayProperties.getAlipay_url(),
                alipayProperties.getApp_id(),
                alipayProperties.getApp_private_key(),
                AlipayProperties.format,//参数的格式json
                AlipayProperties.charset,
                alipayProperties.getAlipay_public_key(),
                AlipayProperties.sign_type);//非对称的加密算法

        return alipayClient;
    }
}
