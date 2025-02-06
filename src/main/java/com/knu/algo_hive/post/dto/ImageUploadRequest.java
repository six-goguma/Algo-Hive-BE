package com.knu.algo_hive.post.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageUploadRequest(
        MultipartFile file
) {
}
