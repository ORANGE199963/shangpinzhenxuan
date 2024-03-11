package com.atguigu.spzx.user.serivce.impl;

import com.atguigu.spzx.common.exp.GuiguException;
import com.atguigu.spzx.user.serivce.SmsService;
import com.atguigu.spzx.user.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void sendCode(String phone) {
        String redisCodeKey = "user:code:" + phone;
        String codeFromRedis = stringRedisTemplate.opsForValue().get(redisCodeKey);
        if (!StringUtils.isEmpty(codeFromRedis)) {
            return;
        }
        String randomCode = this.getRandomCode();
        this.sendCodeToPhone(phone,randomCode);
        stringRedisTemplate.opsForValue().set(redisCodeKey,randomCode,5, TimeUnit.MINUTES);
    }

    private void sendCodeToPhone(String phone, String code) {
        String host = "https://smsv2.market.alicloudapi.com";
        String path = "/sms/sendv2";
        String method = "GET";
        String appcode = "82f1875c750148ba9745ddb551b21af8";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("content", "【智能云】您的验证码是"+code+"。如非本人操作，请忽略本短信");


        HttpResponse response = null;
        try {
            response = HttpUtils.doGet(host, path, method, headers, querys);//发起了一个get类型的请求，去调用第三方短信接口，如果这一步抛出异常，说明接口调用失败
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(response.toString());

        StatusLine statusLine = response.getStatusLine();//响应状态行
        int statusCode = statusLine.getStatusCode();
        if(statusCode==403){
            throw new GuiguException(299,"第三方短信接口被禁用");
        }
    }

    private String getRandomCode() {
        int a = new Random().nextInt(10000);
        if (a < 1000) {
            a += 1000;
        }
        return a + "";
    }
}

