package com.knu.algo_hive.chat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;
    @NotNull
    private String message;
    @NotNull
    @CreatedDate
    private LocalDateTime chatTime;

    protected ChatMessage() {

    }

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
