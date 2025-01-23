package com.knu.algo_hive.chat.repository;

import com.knu.algo_hive.chat.entity.ChatMessage;
import com.knu.algo_hive.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop50ByRoomOrderByChatTimeDesc(Room room);
}
