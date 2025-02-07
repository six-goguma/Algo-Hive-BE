package com.knu.algo_hive.post.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String contents,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String author
) {
}
