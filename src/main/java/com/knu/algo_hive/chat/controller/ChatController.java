//package com.knu.algo_hive.chat.controller;
//
//import com.knu.algo_hive.chat.entity.ChatMessage;
//import com.knu.algo_hive.chat.repository.ChatMessageRepository;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class ChatController {
//
//    private final SimpMessagingTemplate simpMessagingTemplate;
//    private final ChatMessageRepository chatMessageRepository;
//
//    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatMessageRepository chatMessageRepository) {
//        this.simpMessagingTemplate = simpMessagingTemplate;
//        this.chatMessageRepository = chatMessageRepository;
//    }
//
//    @MessageMapping("/chat")
//    public void sendMessage(ChatMessage chatMessage) {
//        // Redis로 메시지를 전송 (브로캐스트)
//        chatMessageRepository.save(chatMessage);
//
//        // 메시지를 "/topic/chatRoom"으로 브로드캐스트
//        simpMessagingTemplate.convertAndSend("/topic/chatRoom", chatMessage);
//    }
//}
