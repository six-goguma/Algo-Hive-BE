package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    private static final String QUEUE_NAME = "myQueue";

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ChatMessageInfo chatMessageInfo) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, chatMessageInfo);
    }
}
