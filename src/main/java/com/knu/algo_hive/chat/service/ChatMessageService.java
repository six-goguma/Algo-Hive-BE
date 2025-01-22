package com.knu.algo_hive.chat.service;

import com.knu.algo_hive.chat.dto.ChatMessageInfo;
import com.knu.algo_hive.chat.dto.ChatMessageRequest;
import com.knu.algo_hive.chat.entity.ChatMessage;
import com.knu.algo_hive.chat.entity.Room;
import com.knu.algo_hive.chat.repository.ChatMessageRepository;
import com.knu.algo_hive.chat.repository.RoomRepository;
import com.knu.algo_hive.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        chatMessageRepository.save(new ChatMessage(chatMessageRequest.sender(), chatMessageRequest.content(), room));

        return new ChatMessageInfo(chatMessageRequest.sender(), chatMessageRequest.content(), roomName);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageInfo> getRecentMessages(String roomName) {
        Room room = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new NotFoundException("roomName에 해당하는 방이 없습니다."));
        return chatMessageRepository.findTop50ByRoomOrderByChatTimeAsc(room).stream()
                .map(ChatMessage -> new ChatMessageInfo(
                        ChatMessage.getUsername(),
                        ChatMessage.getContent(),
                        ChatMessage.getRoom().getRoomName()
                )).collect(Collectors.toList());
    }
}
