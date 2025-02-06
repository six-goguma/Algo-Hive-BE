package com.knu.algo_hive.post.service;

import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.post.dto.UrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.path}")
    private String uploadFolder;
    @Value("${post.image.url}")
    private String imageUrl;

    public UrlResponse uploadImage(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String thumbnailImageName = uuid + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadFolder + thumbnailImageName);
        try {
            file.transferTo(destinationFile);
            return new UrlResponse(imageUrl + thumbnailImageName);
        } catch (IOException e) {

            throw new ConflictException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteImage(String url) {
        int lastIndex = url.lastIndexOf("/");
        String fileName = url.substring(lastIndex + 1);
        File deletedFile = new File(uploadFolder+fileName);

        deletedFile.deleteOnExit();
    }
}
