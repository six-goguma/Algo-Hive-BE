package com.knu.algo_hive.post.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record TagRequest(
        @NotNull
        Set<String> tags
) {
}
