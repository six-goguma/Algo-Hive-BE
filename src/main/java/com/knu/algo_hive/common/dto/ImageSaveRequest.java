package com.knu.algo_hive.common.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageSaveRequest(
        MultipartFile file
) {
}
