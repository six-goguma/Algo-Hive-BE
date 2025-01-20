package com.knu.algo_hive.chat.rabbitmq;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(ChatMessageInfo chatMessageInfo) {
        rabbitTemplate.convertAndSend("myQueue", chatMessageInfo);
        System.out.println("Sent message to RabbitMQ: " + chatMessageInfo);
    }
}
