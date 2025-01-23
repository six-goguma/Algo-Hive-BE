package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    private static final String QUEUE_NAME = "algoQueue";

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendMessage(ChatMessageInfo chatMessageInfo) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, chatMessageInfo);
    }
}
