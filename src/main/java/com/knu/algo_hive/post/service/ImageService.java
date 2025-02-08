package com.knu.algo_hive.post.service;

import com.knu.algo_hive.common.exception.BadRequestException;
import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.post.dto.UrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${file.path}")
    private String uploadFolder;
    @Value("${image.url}")
    private String imageUrl;

    public UrlResponse uploadImage(MultipartFile file, String storageId, String email) {
        String subDirectory = email + "/" + storageId;

        try {
            Path directoryPath = Paths.get(uploadFolder + subDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            throw new ConflictException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadFolder + subDirectory + "/" + fileName);

        try {
            file.transferTo(destinationFile);
            return new UrlResponse(imageUrl + subDirectory + "/" + fileName);
        } catch (IOException e) {
            throw new ConflictException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteImage(String url, String email) {
        // url example: /backend/profile/images/test3@email.com/1234/Frame.png
        String[] parts = url.split("/");
        if (parts.length != 7) throw new BadRequestException(ErrorCode.INVALID_PATH_FORMAT);
        String storageId = parts[5];
        String filename = parts[6];

        File targetFile = new File(uploadFolder + email + "/" + storageId + "/" + filename);
        if (!targetFile.delete()) throw new ConflictException(ErrorCode.IMAGE_DELETE_FAILED);
    }

    public void deleteAllImagesInStorageId(String email, String storageId) {
        Path directoryPath = Paths.get(uploadFolder + email + "/" + storageId);

        if (!Files.exists(directoryPath)) {
            return;
        }

        File directory = directoryPath.toFile();
        File[] images = directory.listFiles();
        if (images != null) {
            for (File image : images) {
                if (!image.delete()) {
                    throw new ConflictException(ErrorCode.IMAGE_DELETE_FAILED);
                }
            }
        }
        if (!directory.delete()) throw new ConflictException(ErrorCode.IMAGE_DELETE_FAILED);
    }
}
