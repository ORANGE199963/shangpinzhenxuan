package com.atguigu.spzx.product.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    //缓存管理器 CacheManager接口 + RedisCacheManager实现类
    //定义了缓存数据的key-value的结构（key是字符串类型，value：json字符串）
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {

        //1、定义key和value序列化方式
        //key的序列化方式（string）
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //value的序列化方式（json）
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        //2、缓存配置：过期时间 + key和value序列化方式
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //过期时间600秒
                .entryTtl(Duration.ofSeconds(600))
                //key 序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                //value 序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer));

        //3、配置RedisCacheManager（缓存管理器）
        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();

        return cacheManager;
    }

}