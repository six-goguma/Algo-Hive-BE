package com.knu.algo_hive.chat.repository;

import com.knu.algo_hive.chat.entity.ChatMessage;
import com.knu.algo_hive.chat.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByRoomOrderByChatTimeDesc(Room room, Pageable pageable);

    @Modifying
    @Query("UPDATE ChatMessage cm SET cm.username = :nickname WHERE cm.userEmail = :email")
    void updateUsernameByUserEmail(@Param("email") String email, @Param("nickname") String nickname);
}
