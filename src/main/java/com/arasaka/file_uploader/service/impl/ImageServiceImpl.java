package com.arasaka.file_uploader.service.impl;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.generated.dto.UploadImageResponseDto;
import com.arasaka.file_uploader.mapper.ImageMapper;
import com.arasaka.file_uploader.service.ImageProcessingService;
import com.arasaka.file_uploader.service.ImageService;
import com.arasaka.file_uploader.service.ImageTransactionService;
import com.arasaka.file_uploader.util.ApacheUtil;
import com.arasaka.file_uploader.util.FileEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing image-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class ImageServiceImpl implements ImageService {

    private final ImageTransactionService imageTransactionService;
    private final ImageProcessingService imageProcessingService;
    private final ImageMapper imageMapper;

    /**
     * Retrieves an ImageEntity by its file name.
     *
     * @param fileName the name of the file to retrieve.
     * @return the ImageEntity associated with the given file name.
     * @throws IllegalArgumentException if the image is not found.
     */
    @Override
    public ImageEntity getEntityByName(String fileName) {
        log.info("Fetching image entity by file name: {}", fileName);
        return imageTransactionService.findImageByFileName(fileName)
                .orElseThrow(() -> {
                    log.error("Image not found with file name: {}", fileName);
                    return new IllegalArgumentException("Image not found");
                });
    }

    /**
     * Downloads all images from the database.
     *
     * @return a list of all ImageEntity objects.
     */
    @Override
    public List<ImageEntity> downloadAllImages() {
        log.info("Downloading all images");
        return imageTransactionService.getAllImages();
    }

    /**
     * Uploads and processes a list of images, resizing them to the specified dimensions.
     *
     * @param images the list of MultipartFile objects to upload.
     * @param width  the target width for resizing the images.
     * @param height the target height for resizing the images.
     * @return a list of UploadImageResponseDto objects representing the uploaded images.
     */
    @Override
    @SneakyThrows
    public List<UploadImageResponseDto> uploadImages(List<MultipartFile> images, Integer width, Integer height) {
        log.info("Starting upload of {} images with target width: {} and height: {}", images.size(), width, height);
        List<ImageEntity> imageEntities = new ArrayList<>();
        for (MultipartFile image : images) {
            try {
                byte[] resizedImage = imageProcessingService.resizeImage(image.getBytes(), width, height);
                log.trace("Image resized successfully");

                String mimeType = ApacheUtil.getMimeTypeFromInputStream(image.getInputStream());
                log.trace("MIME type determined: {}", mimeType);

                ImageEntity imageEntity = ImageEntity.builder()
                        .fileName(getFileName())
                        .fileType(mimeType)
                        .fileSize(resizedImage.length)
                        .encryptedData(FileEncryptionUtil.encryptFileToBytes(resizedImage))
                        .width(width)
                        .height(height)
                        .build();
                imageEntities.add(imageEntity);
            } catch (Exception e) {
                log.error("Error processing image: {}", image.getOriginalFilename(), e);
                throw e;
            }
        }
        log.info("Uploading images to the database");
        return imageMapper.imageEntitiesToUploadImageResponseDtos(imageTransactionService.saveImages(imageEntities));
    }

    /**
     * Generates a unique file name for an image.
     *
     * @return a unique file name.
     */
    String getFileName() {
        String generatedFileName = UUID.randomUUID().toString();
        while (imageTransactionService.findImageByFileName(generatedFileName).isPresent()) {
            log.trace("Generated file name already exists: {}", generatedFileName);
            generatedFileName = UUID.randomUUID().toString();
        }
        log.trace("Generated unique file name: {}", generatedFileName);
        return generatedFileName;
    }
}