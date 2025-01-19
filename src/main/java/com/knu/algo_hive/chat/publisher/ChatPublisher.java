package com.knu.algo_hive.chat.publisher;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    public ChatPublisher(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publishMessage(String message) {
        redisTemplate.convertAndSend("chat", message);  // "chat" 채널에 메시지 발행
    }
}
