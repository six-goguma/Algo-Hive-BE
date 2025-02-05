package com.knu.algo_hive.chat.repository;

import com.knu.algo_hive.chat.entity.ChatMessage;
import com.knu.algo_hive.chat.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByRoomOrderByChatTimeDesc(Room room, Pageable pageable);
}
