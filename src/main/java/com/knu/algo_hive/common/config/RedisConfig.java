package com.knu.algo_hive.common.config;

import com.knu.algo_hive.chat.dto.UsersResponse;
import com.knu.algo_hive.common.properties.RedisProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@AllArgsConstructor
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // UsersResponse를 직렬화/역직렬화하는 Jackson2JsonRedisSerializer 설정
        Jackson2JsonRedisSerializer<UsersResponse> usersResponseSerializer = new Jackson2JsonRedisSerializer<>(UsersResponse.class);

        // Object 타입을 직렬화/역직렬화하는 Jackson2JsonRedisSerializer 설정
        Jackson2JsonRedisSerializer<Object> objectSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(objectSerializer);  // 기본적으로 Object 타입 직렬화기 사용
        // Hash key serializer
        template.setHashKeySerializer(new StringRedisSerializer());
        // Hash value serializer를 Object 타입 직렬화기로 설정
        template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));

        // 특정 타입 (UsersResponse)을 저장하고 가져올 때 사용하려면 직렬화기 명시적으로 설정 필요
        template.setValueSerializer(usersResponseSerializer);  // UsersResponse를 저장할 때만 사용 가능

        template.afterPropertiesSet();
        return template;
    }
}
