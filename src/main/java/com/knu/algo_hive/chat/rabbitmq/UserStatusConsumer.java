package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.UserInRoomResponse;
import com.knu.algo_hive.chat.dto.UsersResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserStatusConsumer {

    private static final String USER_QUEUE_NAME = "allUserQueue";
    private static final String REDIS_USER_KEY = "connectedUsers";

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserStatusConsumer(SimpMessagingTemplate messagingTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(queues = USER_QUEUE_NAME, concurrency = "5-10")
    public void receiveUserStatus(@Payload UserInRoomResponse userInRoomResponse) {
        UsersResponse user = new UsersResponse(userInRoomResponse.userName(), userInRoomResponse.roomName());

        if (userInRoomResponse.isJoin()) {
            redisTemplate.opsForSet().add(REDIS_USER_KEY, user);
        } else {
            redisTemplate.opsForSet().remove(REDIS_USER_KEY, user);
        }

        Set<Object> rawUsers = redisTemplate.opsForSet().members(REDIS_USER_KEY);
        Set<UsersResponse> connectedUsers = rawUsers.stream()
                .map(obj -> (UsersResponse) obj)
                .collect(Collectors.toSet());

        messagingTemplate.convertAndSend("/topic/users", connectedUsers);
    }
}

