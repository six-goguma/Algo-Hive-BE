package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.post.dto.PostRequest;
import com.knu.algo_hive.post.dto.PostResponse;
import com.knu.algo_hive.post.dto.PostSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "게시물_CRUD", description = "게시물 관련 API")
public class PostController {
    @GetMapping("/api/v1/posts")
    @Operation(summary = "모든 게시물 페이지네이션 조회(미구현)",
            description = "페이지네이션 적용. /api/v1/posts/all?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex)좋아요 개수 기준 내림차순 조회 /api/v1/posts/all?page=0&size=10&sort=likeCount,desc"
    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getPostSummaries(@Parameter(hidden = true)
                                                                      @PageableDefault(size = 10, sort = "createdAt,desc")
                                                                      Pageable pageable) {
        PostSummaryResponse post1 = new PostSummaryResponse(1L, "굉장한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약!", LocalDateTime.now(), 777, 777, "김계란");
        PostSummaryResponse post2 = new PostSummaryResponse(0L, "이상한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약~", LocalDateTime.now().minusDays(3), 3, 53, "김김");
        List<PostSummaryResponse> posts = List.of(post1, post2);
        Page<PostSummaryResponse> page = new PageImpl<>(posts, PageRequest.of(0, 5), 2);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/api/v1/{nickname}/posts")
    @Operation(summary = "특정 유저의 게시물 페이지네이션 조회(미구현)",
            description = "페이지네이션 적용. /api/v1/posts?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex)좋아요 개수 기준 내림차순 조회 /api/v1/posts?page=0&size=10&sort=likeCount,desc"

    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getMyPostSummaries(@Parameter(hidden = true)
                                                                        @PageableDefault(size = 10, sort = "createdAt,desc")
                                                                        Pageable pageable,
                                                                        @PathVariable String nickname) {
        PostSummaryResponse post1 = new PostSummaryResponse(1L, "굉장한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약!", LocalDateTime.now(), 777, 777, "아무개");
        PostSummaryResponse post2 = new PostSummaryResponse(0L, "이상한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약~", LocalDateTime.now().minusDays(3), 3, 53, "홍길동");
        List<PostSummaryResponse> posts = List.of(post1, post2);
        Page<PostSummaryResponse> page = new PageImpl<>(posts, PageRequest.of(0, 5), 2);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/api/v1/posts/{postId}")
    @Operation(summary = "게시물 상세 조회(미구현)",
            description = "게시물 상세 조회"
    )
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse postResponse = new PostResponse(0L, "굉장한 제목", "Hello 방가", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약!", 777, 777, LocalDateTime.now(), LocalDateTime.now(), "JAMES");
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping("/api/v1/posts")
    @Operation(summary = "게시물 저장(미구현)",
            description = "게시물 저장"
    )
    public ResponseEntity<Void> savePost(@RequestBody PostRequest request) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v1/posts/{postId}")
    @Operation(summary = "게시물 수정(미구현)",
            description = "게시물 수정"
    )
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody PostRequest request) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/posts/{postId}")
    @Operation(summary = "게시물 삭제(미구현)",
            description = "게시물 삭제"
    )
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v1/posts/tags/{tagName}")
    @Operation(summary = " 태그별로 모든 게시물 페이지네이션 조회(미구현)",
            description = "페이지네이션 적용. /api/v1/posts?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex)좋아요 개수 기준 내림차순 조회 /api/v1/posts?page=0&size=10&sort=likeCount,desc"

    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getAllPostSummariesByTag(@Parameter(hidden = true)
                                                                              @PageableDefault(size = 10, sort = "createdAt,desc")
                                                                              @PathVariable String tagName,
                                                                              Pageable pageable) {

        PostSummaryResponse post1 = new PostSummaryResponse(1L, "굉장한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약!", LocalDateTime.now(), 777, 777, "이름이다");
        PostSummaryResponse post2 = new PostSummaryResponse(0L, "이상한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약~", LocalDateTime.now().minusDays(3), 3, 53, "저자는 나");
        List<PostSummaryResponse> posts = List.of(post1, post2);
        Page<PostSummaryResponse> page = new PageImpl<>(posts, PageRequest.of(0, 5), 2);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/api/v1/{nickname}/posts/tags/{tagName}")
    @Operation(summary = "특정 유저의 태그별 게시물 페이지네이션 조회(미구현)",
            description = "페이지네이션 적용. /api/v1/posts?page={page번호}&size={page content 개수}&sort={content 속성},{desc || asc} ✅ex)좋아요 개수 기준 내림차순 조회 /api/v1/posts?page=0&size=10&sort=likeCount,desc"

    )
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "페이지 번호 (0부터 시작)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "페이지 크기", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 기준 (속성,오름차순|내림차순)", example = "createdAt,desc", schema = @Schema(type = "string"))
    })
    public ResponseEntity<Page<PostSummaryResponse>> getPostSummariesByTag(@Parameter(hidden = true)
                                                                           @PageableDefault(size = 10, sort = "createdAt,desc")
                                                                           @PathVariable String tagName,
                                                                           Pageable pageable,
                                                                           @PathVariable String nickname) {

        PostSummaryResponse post1 = new PostSummaryResponse(1L, "굉장한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약!", LocalDateTime.now(), 777, 777, "방가");
        PostSummaryResponse post2 = new PostSummaryResponse(0L, "이상한 제목", "https://cdn.pixabay.com/photo/2024/02/12/21/34/sunset-8569636_1280.jpg", "요약~", LocalDateTime.now().minusDays(3), 3, 53, "하하");
        List<PostSummaryResponse> posts = List.of(post1, post2);
        Page<PostSummaryResponse> page = new PageImpl<>(posts, PageRequest.of(0, 5), 2);
        return ResponseEntity.ok(page);
    }
}
