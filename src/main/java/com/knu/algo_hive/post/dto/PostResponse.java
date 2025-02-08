package com.knu.algo_hive.post.dto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String contents,
        String thumbnail,
        String summary,
        int likeCount,
        int commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String author
) {
}
