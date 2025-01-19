package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.post.dto.CommentResponse;
import com.knu.algo_hive.post.dto.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "게시물_댓글", description = "게시물의 댓글 관련 API")
public class CommentController {

    @GetMapping("/api/v1/posts/{postId}/comments")
    @Operation(summary = "특정 게시물의 댓글 페이지네이션 조회(미구현)",
            description = "페이지네이션 적용. /api/v1/posts/{postId}/comments?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex) 생성일 기준 내림차순 조회(최신 댓글 부터) /api/v1/posts/{postId}/comments?page=0&size=10&sort=createdAt,desc"

    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable Long postId) {

        CommentResponse comment1 = new CommentResponse(0L, "좋은데여? 굳", LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(4), "물통");
        CommentResponse comment2 = new CommentResponse(0L, "별로임...", LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(4), "물병");
        List<CommentResponse> posts = List.of(comment1, comment2);
        Page<CommentResponse> page = new PageImpl<>(posts, PageRequest.of(0, 5), 2);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/api/v1/posts/{postId}/comments")
    @Operation(summary = "댓글 저장 (미구현)",
            description = "댓글 달기"
    )
    public ResponseEntity<Void> saveComment(@RequestBody CommentRequest request,
                                            @PathVariable Long postId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/posts/{postId}/comments")
    @Operation(summary = "댓글 수정 (미구현)",
            description = "댓글 수정하기"
    )
    public ResponseEntity<Void> updateComment(@RequestBody CommentRequest request,
                                              @PathVariable Long postId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/{postId}/comments")
    @Operation(summary = "댓글 삭제 (미구현)",
            description = "댓글 삭제하기"
    )
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId) {
        return ResponseEntity.ok().build();
    }
}
