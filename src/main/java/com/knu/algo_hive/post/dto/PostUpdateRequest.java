package com.knu.algo_hive.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostUpdateRequest(
        @NotBlank
        String title,
        @NotNull
        String contents,
        @NotNull
        String thumbnail,
        @NotNull
        String summary
) {
}
