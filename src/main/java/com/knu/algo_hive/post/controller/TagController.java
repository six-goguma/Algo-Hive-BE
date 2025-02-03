package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExamples;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.post.dto.TagRequest;
import com.knu.algo_hive.post.dto.TagResponse;
import com.knu.algo_hive.post.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "게시물_태그", description = "게시물의 태그 관련 API")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/api/v1/posts/{postId}/tags")
    @Operation(summary = "게시물 태그 조회",
            description = "게시물 태그 조회"
    )
    public ResponseEntity<TagResponse> getTags(@PathVariable Long postId) {
        return ResponseEntity.ok(tagService.getTags(postId));
    }

    @PostMapping("/api/v1/posts/{postId}/tags")
    @Operation(summary = "게시물 태그 저장",
            description = "태그 저장"
    )
    @ApiErrorCodeExamples({ErrorCode.POST_NOT_FOUND, ErrorCode.NOT_YOUR_RESOURCE})
    public ResponseEntity<Void> addTags(@PathVariable Long postId,
                                        @RequestBody TagRequest request,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.saveOrUpdateTags(postId, request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/posts/{postId}/tags")
    @Operation(summary = "게시물 태그 수정",
            description = "태그 수정"
    )
    @ApiErrorCodeExamples({ErrorCode.POST_NOT_FOUND, ErrorCode.NOT_YOUR_RESOURCE})
    public ResponseEntity<Void> updateTags(@PathVariable Long postId,
                                           @RequestBody TagRequest request,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.saveOrUpdateTags(postId, request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
