package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
    private static final String QUEUE_NAME = "algoQueue";

    private final SimpMessagingTemplate messagingTemplate;

    public MessageConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = QUEUE_NAME, concurrency = "5-10")
    public void receiveMessage(ChatMessageInfo chatMessageInfo) {

        messagingTemplate.convertAndSend("/topic/" + chatMessageInfo.roomName(), chatMessageInfo);
    }
}
