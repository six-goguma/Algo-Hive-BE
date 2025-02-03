package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExamples;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.post.dto.LikeCountResponse;
import com.knu.algo_hive.post.dto.LikeStatusResponse;
import com.knu.algo_hive.post.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "게시물_좋아요", description = "게시물의 좋아요 관련 API")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/api/v1/posts/{postId}/likes/count")
    @Operation(summary = "좋아요 개수 조회",
            description = "좋아요 개수 조회"
    )
    public ResponseEntity<LikeCountResponse> getLikeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }

    @PutMapping("/api/v1/posts/{postId}/likes/status")
    @Operation(summary = "좋아요 상태 변경(좋아요/좋아요 취소)",
            description = "좋아요 상태 변경. api 요청마다 상태가 반전됨. 200 응답시 현 좋아요 상태 반환"
    )
    @ApiErrorCodeExamples({ErrorCode.CONCURRENCY_CONFLICT, ErrorCode.MEMBER_NOT_FOUND, ErrorCode.POST_NOT_FOUND})
    public ResponseEntity<LikeStatusResponse> changeLikeStatus(@PathVariable Long postId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(likeService.changeLikeStatusWithRetry(postId, userDetails.getUsername()));
    }

    @GetMapping("/api/v1/posts/{postId}/likes/status")
    @Operation(summary = " 좋아요 현재 상태 조회 ",
            description = "현재 유저의 해당 포스트 좋아요 상태 조회. 200 응답시 현 좋아요 상태 반환"
    )
    public ResponseEntity<LikeStatusResponse> getLikeStatus(@PathVariable Long postId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(likeService.getLikeStatus(postId, userDetails.getUsername()));
    }
}
