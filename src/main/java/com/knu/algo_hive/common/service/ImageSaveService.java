package com.knu.algo_hive.common.service;

import com.knu.algo_hive.common.dto.ImageUrlResponse;
import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageSaveService {
    @Value("${file.path}")
    private String uploadFolder;
    @Value("${image.url}")
    private String imageUrl;
    @Value("${server.url}")
    private String serverUrl;

    public ImageUrlResponse uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadFolder + "/" + fileName);

        try {
            file.transferTo(destinationFile);
            return new ImageUrlResponse(serverUrl + imageUrl + fileName);
        } catch (IOException e) {
            throw new ConflictException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteImage(String filename) {
        File targetFile = new File(uploadFolder + "/" + filename);
        if (!targetFile.delete()) throw new ConflictException(ErrorCode.IMAGE_DELETE_FAILED);
    }
}
