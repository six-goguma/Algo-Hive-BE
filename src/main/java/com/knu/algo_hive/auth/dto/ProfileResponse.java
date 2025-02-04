package com.knu.algo_hive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProfileResponse(@Schema(description = "이미지 주소, 프론트에서 앞에 백엔드 서버 주소 붙여야됩니다.",
        example = "21410765-e1b6-4e0a-8108-55d133b62959_example.png")
                              String url) {
}
