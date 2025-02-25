package com.knu.algo_hive.common.controller;

import com.knu.algo_hive.common.dto.ImageSaveRequest;
import com.knu.algo_hive.common.dto.ImageUrlResponse;
import com.knu.algo_hive.common.service.ImageSaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "[관리자용]", description = "관리자용 API")
public class ImageSaveController {
    private final ImageSaveService imageSaveService;

    public ImageSaveController(ImageSaveService imageSaveService) {
        this.imageSaveService = imageSaveService;
    }

    @PostMapping(value = "/api/v1/server/images/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "서버에 이미지 업로드 (관리자용)", description = "서버에 공통적으로 사용 가능한 이미지를 업로드한다")
    public ResponseEntity<ImageUrlResponse> getImageUrl(@ModelAttribute ImageSaveRequest request) {
        return ResponseEntity.ok(imageSaveService.uploadImage(request.file()));
    }
}
