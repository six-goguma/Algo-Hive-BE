package com.knu.algo_hive.ai.controller;

import com.knu.algo_hive.ai.dto.GeminiApiRequest;
import com.knu.algo_hive.ai.dto.GeminiApiResponse;
import com.knu.algo_hive.ai.service.GeminiApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI", description = "코드 분석 및 블로그 글 작성 AI 관련 api")
public class GeminiApiController {

    private final GeminiApiService geminiApiService;

    public GeminiApiController(GeminiApiService geminiApiService) {
        this.geminiApiService = geminiApiService;
    }

    @Operation(summary = "AI 기반 코드 분석 평가", description = "알고리즘 코드를 입력하면 Gemini AI를 활용하여 분석 결과를 velog 형식으로 리턴합니다.")
    @PostMapping("/analyze")
    public ResponseEntity<GeminiApiResponse> analyzeCode(@RequestBody GeminiApiRequest geminiApiRequest) throws Exception {
        GeminiApiResponse geminiApiResponse = geminiApiService.analyzeCode(geminiApiRequest);
        return ResponseEntity.ok().body(geminiApiResponse);
    }
}
