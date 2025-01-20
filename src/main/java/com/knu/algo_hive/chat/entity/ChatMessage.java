package com.knu.algo_hive.chat.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;
    @NotNull
    private String content;
    @NotNull
    @CreatedDate
    private LocalDateTime chatTime;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    protected ChatMessage() {

    }

    public ChatMessage(String username, String content, Room room) {
        this.username = username;
        this.content = content;
        this.room = room;
    }
}
