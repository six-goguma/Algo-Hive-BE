package com.knu.algo_hive.chat.dto;

public record ChatMessageRequest(
        String sender,
        String email,
        String content
) {
}
