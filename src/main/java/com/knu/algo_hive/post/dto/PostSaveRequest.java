package com.knu.algo_hive.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostSaveRequest(
        @NotBlank
        String title,
        @NotNull
        String content,
        @NotNull
        String thumbnail,
        @NotNull
        String summary
) {
}
