package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExamples;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.post.dto.ImageDeleteRequest;
import com.knu.algo_hive.post.dto.ImageUploadRequest;
import com.knu.algo_hive.post.dto.UrlResponse;
import com.knu.algo_hive.post.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "이미지 관리", description = "이미지 관련 API")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/api/v1/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드(png, jpeg 그리고 10MB 까지 업로드 가능)", description = "로그인 필요")
    @ApiErrorCodeExamples({ErrorCode.IMAGE_NOT_UPLOADED, ErrorCode.FILE_SIZE_EXCEEDED, ErrorCode.INVALID_FILE_TYPE, ErrorCode.IMAGE_UPLOAD_FAILED})
    public ResponseEntity<UrlResponse> getImageUrl(@ModelAttribute ImageUploadRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(imageService.uploadImage(request.file(), request.storageId(), userDetails.getUsername()));
    }

    @PostMapping("/api/v1/images/delete")
    @Operation(summary = "이미지 제거(업로드 api를 통해 받는 url을 body에)", description = "로그인 필요")
    @ApiErrorCodeExamples({ErrorCode.INVALID_PATH_FORMAT, ErrorCode.IMAGE_DELETE_FAILED})
    public ResponseEntity<Void> deleteImage(@RequestBody ImageDeleteRequest request,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        imageService.deleteImage(request.url(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
