package com.knu.algo_hive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(@Schema(description = "사용자 닉네임", example = "user1") String nickname) {
}
