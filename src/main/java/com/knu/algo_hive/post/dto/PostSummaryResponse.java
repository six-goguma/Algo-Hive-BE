package com.knu.algo_hive.post.dto;

import java.time.LocalDateTime;

public record PostSummaryResponse(
        Long id,
        String title,
        String thumbnail,
        String summary,
        LocalDateTime createdAt,
        int likeCount,
        int commentCount,
        String author
) {
}
