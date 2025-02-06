package com.knu.algo_hive.ai.controller;

import com.knu.algo_hive.ai.dto.AiReportResponse;
import com.knu.algo_hive.ai.dto.GeminiApiRequest;
import com.knu.algo_hive.ai.dto.GeminiApiResponse;
import com.knu.algo_hive.ai.service.GeminiApiService;
import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.common.dto.StringTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI", description = "코드 분석 및 블로그 글 작성 AI 관련 API")
public class GeminiApiController {

    private final GeminiApiService geminiApiService;

    public GeminiApiController(GeminiApiService geminiApiService) {
        this.geminiApiService = geminiApiService;
    }

    @Operation(summary = "AI 기반 코드 분석 평가", description = "알고리즘 코드를 입력하면 Gemini AI를 활용하여 분석 결과를 velog 형식으로 리턴합니다. 로그인 필수")
    @PostMapping("/analyze")
    public ResponseEntity<GeminiApiResponse> analyzeCode(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestBody GeminiApiRequest geminiApiRequest) throws Exception {
        GeminiApiResponse geminiApiResponse = geminiApiService.analyzeCode(geminiApiRequest, userDetails.getUsername());
        return ResponseEntity.ok().body(geminiApiResponse);
    }

    @Operation(summary = "유저의 AI 리포트 모두 가져오기", description = "유저의 모든 AI 리포트를 Page로 가져옵니다. 로그인 필수")
    @GetMapping("/reports")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdDateTime,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<AiReportResponse>> getAllAiReportsByMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                          @Parameter(hidden = true)
                                                                          @PageableDefault(sort = "createdDateTime", direction = Sort.Direction.DESC)
                                                                          Pageable pageable) {
        Page<AiReportResponse> aiReportResponses = geminiApiService.getAllAiReports(userDetails.getUsername(), pageable);
        return ResponseEntity.ok().body(aiReportResponses);
    }

    @Operation(summary = "유저의 특정 AI 리포트 가져오기", description = "aiReportId에 해당하는 AI 리포트를 가져옵니다. 로그인 필수")
    @GetMapping("/reports/{aiReportId}")
    public ResponseEntity<AiReportResponse> getAiReportById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable("aiReportId") Long aiReportId) {
        AiReportResponse aiReportResponse = geminiApiService.getAiReportById(userDetails.getUsername(), aiReportId);
        return ResponseEntity.ok().body(aiReportResponse);
    }

    @Operation(summary = "AI 리포트 삭제", description = "aiReportId에 해당하는 AI 리포트를 삭제합니다. 로그인 필수")
    @DeleteMapping("/reports/{aiReportId}")
    public ResponseEntity<StringTypeResponse> deleteAiReportById(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("aiReportId") Long aiReportId) {
        geminiApiService.deleteAiReport(userDetails.getUsername(), aiReportId);
        return ResponseEntity.ok().body(new StringTypeResponse("삭제되었습니다."));
    }
}
