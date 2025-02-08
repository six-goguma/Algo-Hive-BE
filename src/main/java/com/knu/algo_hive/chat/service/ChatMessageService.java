package com.knu.algo_hive.chat.service;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import com.knu.algo_hive.chat.dto.ChatMessageRequest;
import com.knu.algo_hive.chat.entity.ChatMessage;
import com.knu.algo_hive.chat.entity.Room;
import com.knu.algo_hive.chat.repository.ChatMessageRepository;
import com.knu.algo_hive.chat.repository.RoomRepository;
import com.knu.algo_hive.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final RoomRepository roomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, RoomRepository roomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public ChatMessageInfo convertAndSaveChatMessage(ChatMessageRequest chatMessageRequest, String roomName) {

        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new NotFoundException("해당 roomName은 존재하지 않습니다."));

        chatMessageRepository.save(new ChatMessage(chatMessageRequest.sender(), chatMessageRequest.email(), chatMessageRequest.content(), room));

        return new ChatMessageInfo(chatMessageRequest.sender(), chatMessageRequest.email(), chatMessageRequest.content(), roomName);
    }

    @Transactional
    public Page<ChatMessageInfo> getRecentMessages(String roomName, Pageable pageable) {
        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new NotFoundException("roomName에 해당하는 방이 없습니다."));

        Page<ChatMessage> chatMessages = chatMessageRepository.findByRoomOrderByChatTimeDesc(room, pageable);
        return chatMessages.map(chatMessage -> new ChatMessageInfo(
                chatMessage.getUsername(),
                chatMessage.getUserEmail(),
                chatMessage.getContent(),
                chatMessage.getRoom().getRoomName()
        ));
    }

    @Transactional
    public void changeChatNickname(String email, String nickname) {
        chatMessageRepository.updateUsernameByUserEmail(email, nickname);
    }
}
