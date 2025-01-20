package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "myQueue")
    public void receiveMessage(ChatMessageInfo chatMessageInfo) {
        System.out.println("Received message from RabbitMQ: " + chatMessageInfo);

        messagingTemplate.convertAndSend("/topic/messages/" + chatMessageInfo.roomName(), chatMessageInfo);
    }
}
