package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.post.dto.LikeCountResponse;
import com.knu.algo_hive.post.dto.LikeStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "게시물_좋아요", description = "게시물의 좋아요 관련 API")
public class LikeController {

    @GetMapping("/api/v1/posts/{postId}/likes/count")
    @Operation(summary = "좋아요 개수 조회(미구현)",
            description = "좋아요 개수 조회"
    )
    public ResponseEntity<LikeCountResponse> getLikeCount(@PathVariable String postId) {
        LikeCountResponse likeCountResponse = new LikeCountResponse(34);
        return ResponseEntity.ok(likeCountResponse);
    }

    @PutMapping("/api/v1/posts/{postId}/likes/status")
    @Operation(summary = "좋아요 상태 변경(좋아요/좋아요 취소) (미구현)",
            description = "좋아요 상태 변경. api 요청마다 상태가 반전됨. 200 응답시 현 좋아요 상태 반환"
    )
    public ResponseEntity<LikeStatusResponse> changeLikeStatus(@PathVariable String postId) {
        return ResponseEntity.ok(new LikeStatusResponse(true));
    }

    @GetMapping("/api/v1/posts/{postId}/likes/status")
    @Operation(summary = " 좋아요 상태 조회 (미구현)",
            description = "현재 유저의 해당 포스트 좋아요 상태 조회. 200 응답시 현 좋아요 상태 반환"
    )
    public ResponseEntity<LikeStatusResponse> getLikeStatus(@PathVariable Long postId) {
        return ResponseEntity.ok(new LikeStatusResponse(true));
    }
}
