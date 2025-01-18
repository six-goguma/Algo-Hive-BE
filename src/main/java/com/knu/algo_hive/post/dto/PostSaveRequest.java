package com.knu.algo_hive.post.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PostSaveRequest(
        @NotEmpty
        @NotNull
        String title,
        @NotEmpty
        @NotNull
        String content,
        @NotNull
        String thumbnail,
        @NotNull
        String summary
) {
}
