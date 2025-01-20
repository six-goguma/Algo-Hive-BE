package com.knu.algo_hive.chat.controller;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import com.knu.algo_hive.chat.dto.ChatMessageRequest;
import com.knu.algo_hive.chat.dto.UserNameRequest;
import com.knu.algo_hive.chat.rabbitmq.MessageProducer;
import com.knu.algo_hive.chat.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    private final MessageProducer messageProducer;
    private final ChatMessageService chatMessageService;

    public WebSocketChatController(MessageProducer messageProducer, ChatMessageService chatMessageService) {
        this.messageProducer = messageProducer;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/{roomName}")
    public void sendMessage(@Payload ChatMessageRequest chatMessageRequest, @DestinationVariable("roomName") String roomName) {

        ChatMessageInfo chatMessageInfo = chatMessageService.convertAndSaveChatMessage(chatMessageRequest, roomName);

        messageProducer.sendMessage(chatMessageInfo);
    }

    @MessageMapping("/chat/setName/{roomName}")
    public void setUserName(@Payload UserNameRequest userNameRequest, @DestinationVariable("roomName") String roomName, SimpMessageHeaderAccessor headerAccessor) {

        String sender = (String) headerAccessor.getSessionAttributes().get("username");

        if (sender == null) {
            return;
        }

        ChatMessageInfo chatMessageInfo = new ChatMessageInfo(sender, userNameRequest.userName(), roomName);

        messageProducer.sendMessage(chatMessageInfo);
    }
}
