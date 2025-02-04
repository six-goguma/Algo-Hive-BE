package com.knu.algo_hive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public record ProfileRequest(@Schema(description = "프로필 이미지 파일")MultipartFile file) {
}
