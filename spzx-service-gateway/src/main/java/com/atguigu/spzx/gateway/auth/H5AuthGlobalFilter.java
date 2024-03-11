package com.atguigu.spzx.gateway.auth;

import com.alibaba.fastjson2.JSON;
import com.atguigu.spzx.model.entity.user.UserInfo;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class H5AuthGlobalFilter implements Ordered, GlobalFilter {

    //spring-util包下提供的一个用于格式校验的工具类
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("当前网关：" + Thread.currentThread().getName());

        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String path = uri.getPath(); //  /api/user/xxxx
        System.out.println(path);

        boolean match = antPathMatcher.match("/api/**/auth/**", path);

        //从redis中获取用户信息
        List<String> list = request.getHeaders().get("token");
        String token = "";
        if(!CollectionUtils.isEmpty(list)){
            token = list.get(0);
        }
        String key = "user:login:"+token;
        //可能返回的是null
        String jsonString = stringRedisTemplate.opsForValue().get(key);
        UserInfo userInfo = JSON.parseObject(jsonString, UserInfo.class);

        if(match && userInfo==null){
            System.out.println("需要校验登录状态");
            //return 当前用户未登录 {code:208} ,  拦截请求并做出响应
            return this.out208(exchange);
        }

        //如果当前用户已经登录，则将会话状态恢复到30分钟（这一步和前端调用哪个接口无关）
        if (userInfo!=null){
            stringRedisTemplate.expire(key,30, TimeUnit.MINUTES);
        }

        //放行
        return chain.filter(exchange);
    }

    private Mono<Void> out208(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();


        Result result = Result.build(null, ResultCodeEnum.LOGIN_AUTH);
//        JSONObject.toJSONString(result);
        String jsonString = JSON.toJSONString(result);//转成json字符串  { code:208,message:'用户未登录',data:null }

        byte[] bits = jsonString.getBytes(StandardCharsets.UTF_8);//json字符串转成字节数组

        //将字节数组包装成一个DataBuffer对象
        DataBuffer buffer = response.bufferFactory().wrap(bits);

        //设置响应头，指定Content-Type响应内容类型（json）
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");


        return response.writeWith(Mono.just(buffer));//给客户端响应
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
