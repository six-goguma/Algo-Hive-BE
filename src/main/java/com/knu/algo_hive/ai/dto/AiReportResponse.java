package com.knu.algo_hive.ai.dto;

import java.time.LocalDateTime;

public record AiReportResponse(
        Long id,
        LocalDateTime createdDateTime,
        String code,
        String response
) {
}
