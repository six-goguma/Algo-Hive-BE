package com.knu.algo_hive.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(as = UsersResponse.class)
public record UsersResponse(
        String userName,
        String roomName
) implements Serializable {
}
