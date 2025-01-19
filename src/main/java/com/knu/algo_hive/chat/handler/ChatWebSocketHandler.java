package com.knu.algo_hive.chat.handler;

import com.knu.algo_hive.chat.entity.ChatMessage;
import com.knu.algo_hive.chat.publisher.ChatPublisher;
import com.knu.algo_hive.chat.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final ChatMessageRepository chatMessageRepository;
    private final ChatPublisher chatPublisher;

    public ChatWebSocketHandler(ChatMessageRepository chatMessageRepository, @Lazy ChatPublisher chatPublisher) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatPublisher = chatPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        logger.info("New connection established: sessionId = {}", sessionId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userMessage = message.getPayload();

        if (session.getAttributes().get("username") == null) {
            String username = userMessage;

            if (sessionMap.containsKey(username)) {
                try {
                    session.sendMessage(new TextMessage("username 중복"));
                    session.close();
                } catch (Exception e) {
                    logger.error("Error closing session for duplicate username: {}", username, e);
                }
                return;
            }

            session.getAttributes().put("username", username);
            sessionMap.put(username, session);

            broadcastMessage("💬 " + username + "님이 채팅방에 입장하셨습니다!");
            logger.info("Username {} associated with sessionId {}", username, session.getId());
            return;
        }

        String username = (String) session.getAttributes().get("username");

        if (username == null) {
            logger.warn("Error: Username not found in session.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        ChatMessage chatMessage = new ChatMessage(username, userMessage);
        chatMessageRepository.save(chatMessage);

        // Redis에 메시지 발행
        chatPublisher.publishMessage("[" + formattedTime + "] " + username + ": " + userMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = (String) session.getAttributes().get("username");

        if (username != null) {
            sessionMap.remove(username);

            broadcastMessage("💬 " + username + "님이 채팅방을 떠났습니다.");
            logger.info("User {} disconnected.", username);
        }
    }

    private void broadcastMessage(String message) {
        for (WebSocketSession session : sessionMap.values()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                logger.error("Error broadcasting message: {}", message, e);
            }
        }
    }
}
