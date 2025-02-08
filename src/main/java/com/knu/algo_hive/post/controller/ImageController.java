package com.knu.algo_hive.post.controller;

import com.knu.algo_hive.post.dto.ImageDeleteRequest;
import com.knu.algo_hive.post.dto.ImageUploadRequest;
import com.knu.algo_hive.post.dto.UrlResponse;
import com.knu.algo_hive.post.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "이미지 관리", description = "이미지 관련 API")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/api/v1/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드")
    public ResponseEntity<UrlResponse> getImageUrl(@ModelAttribute ImageUploadRequest request) {
        return ResponseEntity.ok(imageService.uploadImage(request.file()));
    }

    @PostMapping("/api/v1/images/delete")
    @Operation(summary = "이미지 제거(업로드 api를 통해 받는 url을 body에)")
    public ResponseEntity<Void> deleteImage(@RequestBody ImageDeleteRequest request) {
        imageService.deleteImage(request.url());
        return ResponseEntity.ok().build();
    }
}
