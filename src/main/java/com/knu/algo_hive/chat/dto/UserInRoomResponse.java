package com.knu.algo_hive.chat.dto;

public record UserInRoomResponse(
        String userName,
        String roomName,
        boolean isJoin
) {
}
