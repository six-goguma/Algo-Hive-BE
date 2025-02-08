package com.knu.algo_hive.chat.dto;

public record ChatMessageInfo(
        String sender,
        String email,
        String content,
        String roomName
) {
}
