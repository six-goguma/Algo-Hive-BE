package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.post.dto.TagRequest;
import com.knu.algo_hive.post.dto.TagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@Tag(name = "게시물_태그", description = "게시물의 태그 관련 API")
public class TagController {

    @GetMapping("/api/v1/posts/{postId}/tags")
    @Operation(summary = "게시물 태그 조회(미구현)",
            description = "게시물 태그 조회"
    )
    public ResponseEntity<TagResponse> getTags(@PathVariable Long postId) {
        TagResponse tagResponse = new TagResponse(new HashSet<String>(List.of("질문", "홍보", "취업")));
        return ResponseEntity.ok(tagResponse);
    }

    @PostMapping("/api/v1/posts/{postId}/tags")
    @Operation(summary = "게시물 태그 저장(미구현)",
            description = "태그 저장"
    )
    public ResponseEntity<Void> addTags(@PathVariable Long postId,
                                        @RequestBody TagRequest request) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/posts/{postId}/tags")
    @Operation(summary = "게시물 태그 수정(미구현)",
            description = "태그 수정"
    )
    public ResponseEntity<Void> updateTags(@PathVariable Long postId,
                                           @RequestBody TagRequest request) {
        return ResponseEntity.ok().build();
    }
}
