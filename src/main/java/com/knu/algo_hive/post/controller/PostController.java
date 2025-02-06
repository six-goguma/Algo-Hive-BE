package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExamples;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.post.dto.PostRequest;
import com.knu.algo_hive.post.dto.PostResponse;
import com.knu.algo_hive.post.dto.PostSummaryResponse;
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
@Tag(name = "게시글_CRUD", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/api/v1/posts")
    @Operation(summary = "모든 게시글 페이지네이션 조회",
            description = "페이지네이션 적용. /api/v1/posts?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex)좋아요 개수 기준 내림차순 조회 /api/v1/posts?page=0&size=10&sort=likeCount,desc"
    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getPostSummaries(@Parameter(hidden = true)
                                                                      @PageableDefault(sort = "createdAt,desc")
                                                                      Pageable pageable) {
        return ResponseEntity.ok(postService.getPostSummaries(pageable));
    }

    @GetMapping("/api/v1/{nickname}/posts")
    @Operation(summary = "특정 유저의 게시글 페이지네이션 조회",
            description = "페이지네이션 적용. /api/v1/{nickname}/posts?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex)좋아요 개수 기준 내림차순 조회 /api/v1/{nickname}/posts?page=0&size=10&sort=likeCount,desc"
    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getPostSummariesByNickname(@PageableDefault(sort = "createdAt,desc")
                                                                                Pageable pageable,
                                                                                @PathVariable String nickname) {
        return ResponseEntity.ok(postService.getPostSummariesByNickname(pageable, nickname));
    }

    @GetMapping("/api/v1/posts/{postId}")
    @Operation(summary = "게시글 상세 조회",
            description = "게시글 상세 조회"
    )
    @ApiErrorCodeExamples({ErrorCode.POST_NOT_FOUND})
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping("/api/v1/posts")
    @Operation(summary = "게시글 저장",
            description = "게시글 저장"
    )
    @ApiErrorCodeExamples({ErrorCode.MEMBER_NOT_FOUND})
    public ResponseEntity<Void> savePost(@RequestBody PostRequest request,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.savePost(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/posts/{postId}")
    @Operation(summary = "게시글 수정",
            description = "게시글 수정"
    )
    @ApiErrorCodeExamples({ErrorCode.POST_NOT_FOUND, ErrorCode.NOT_YOUR_RESOURCE})
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody PostRequest request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.updatePost(postId, request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/{postId}")
    @Operation(summary = "게시글 삭제",
            description = "게시글 삭제"
    )
    @ApiErrorCodeExamples({ErrorCode.POST_NOT_FOUND, ErrorCode.NOT_YOUR_RESOURCE})
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(postId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/posts/tags/{tagId}")
    @Operation(summary = " 태그별로 모든 게시글 페이지네이션 조회",
            description = "페이지네이션 적용. /api/v1/posts/tags/{tagId}?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc}"
    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getAllPostSummariesByTagId(@PageableDefault(sort = "createdAt,desc")
                                                                                @PathVariable int tagId,
                                                                                Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPostSummariesByTag(tagId, pageable));
    }

    @GetMapping("/api/v1/{nickname}/posts/tags/{tagId}")
    @Operation(summary = "특정 유저의 태그별 게시글 페이지네이션 조회",
            description = "페이지네이션 적용. /api/v1/{nickname}/posts/tags/{tagId}?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc}"
    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getPostSummariesByTagIdAndNickname(@PageableDefault(sort = "createdAt,desc")
                                                                                        @PathVariable int tagId,
                                                                                        @PathVariable String nickname,
                                                                                        Pageable pageable
    ) {
        return ResponseEntity.ok(postService.getPostSummariesByTagIdAndNickname(tagId, nickname, pageable));
    }
}
