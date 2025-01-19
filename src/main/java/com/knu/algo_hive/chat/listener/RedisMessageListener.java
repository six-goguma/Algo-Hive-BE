package com.knu.algo_hive.chat.listener;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisMessageListener implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisMessageListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String chatMessage = new String(message.getBody());
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);  // "/topic/chat"으로 메시지 전송
    }
}
