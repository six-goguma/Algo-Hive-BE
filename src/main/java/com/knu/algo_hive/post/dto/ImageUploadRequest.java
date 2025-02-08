package com.knu.algo_hive.post.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequest(
        MultipartFile file,
        @NotBlank
        String storageId
) {
}
