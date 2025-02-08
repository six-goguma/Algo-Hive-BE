package com.knu.algo_hive.post.dto;

import jakarta.validation.constraints.NotBlank;

public record ImageDeleteRequest(
        @NotBlank
        String url
) {
}
