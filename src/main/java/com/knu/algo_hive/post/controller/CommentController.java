package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.post.dto.CommentRequest;
import com.knu.algo_hive.post.dto.CommentResponse;
import com.knu.algo_hive.post.service.CommentService;
import com.knu.algo_hive.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "게시물_댓글", description = "게시물의 댓글 관련 API")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping("/api/v1/posts/{postId}/comments")
    @Operation(summary = "특정 게시물의 댓글 페이지네이션 조회")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<CommentResponse>> getComments(@PathVariable Long postId,
                                                             @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(commentService.getComments(postId, pageable));
    }

    @PostMapping("/api/v1/posts/{postId}/comments")
    @Operation(summary = "댓글 저장",
            description = "댓글 달기"
    )
    public ResponseEntity<Void> saveComment(@RequestBody CommentRequest request,
                                            @PathVariable Long postId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.saveComment(request, postId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/posts/comments/{commentId}")
    @Operation(summary = "댓글 수정",
            description = "댓글 수정하기"
    )
    public ResponseEntity<Void> updateComment(@RequestBody CommentRequest request,
                                              @PathVariable Long commentId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.updateComment(request, commentId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/comments/{commentId}")
    @Operation(summary = "댓글 삭제",
            description = "댓글 삭제하기"
    )
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
