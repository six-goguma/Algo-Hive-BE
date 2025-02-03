package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.UserInRoomResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Service
public class UserStatusProducer {

    private static final String USER_QUEUE_NAME = "chatUsersQueue";

    private final RabbitTemplate rabbitTemplate;

    public UserStatusProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendUserJoinMessage(String userName, String roomName, StompHeaderAccessor headerAccessor) {
        UserInRoomResponse userInRoomResponse = new UserInRoomResponse(userName, roomName, true);

        headerAccessor.getSessionAttributes().put("username", userName);
        headerAccessor.getSessionAttributes().put("roomName", roomName);

        rabbitTemplate.convertAndSend(USER_QUEUE_NAME, userInRoomResponse);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomName = (String) headerAccessor.getSessionAttributes().get("roomName");

        if (username != null && roomName != null) {
            UserInRoomResponse userInRoomResponse = new UserInRoomResponse(username, roomName, false);
            rabbitTemplate.convertAndSend(USER_QUEUE_NAME, userInRoomResponse);
        }
    }
}
