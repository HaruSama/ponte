package com.arasaka.file_uploader.controller;

import com.arasaka.file_uploader.generated.api.ImageControllerApi;
import com.arasaka.file_uploader.generated.dto.UploadImageResponseDto;
import com.arasaka.file_uploader.service.ImageService;
import com.arasaka.file_uploader.util.ResourceUtil;
import com.arasaka.file_uploader.util.SimpleValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for handling image-related operations.
 */
@RestController
@RequiredArgsConstructor
public class ImageController implements ImageControllerApi {

    private final ImageService imageService;

    /**
     * Downloads all images as a ZIP file.
     *
     * @return ResponseEntity containing the ZIP file of all images.
     */
    @Override
    public ResponseEntity<Resource> _downloadAllImages() {
        return ResourceUtil.ImagesInZipToResponseEntity(imageService.downloadAllImages());
    }

    /**
     * Downloads a specific image by its name.
     *
     * @param imageName the name of the image to download.
     * @return ResponseEntity containing the requested image.
     */
    @Override
    public ResponseEntity<Resource> _downloadImage(String imageName) {
        return ResourceUtil.ImageEntityToResponseEntity(imageService.getEntityByName(imageName));
    }

    /**
     * Uploads a list of images and resizes them to the specified width and height.
     *
     * @param images the list of images to upload.
     * @param width  the width to resize the images to.
     * @param height the height to resize the images to.
     * @return ResponseEntity containing the response DTOs for the uploaded images.
     * @throws IllegalArgumentException if any of the images have an invalid file type.
     */
    @Override
    public ResponseEntity<List<UploadImageResponseDto>> _uploadImages(Integer width, Integer height, List<MultipartFile> images) {
        if (!SimpleValidatorUtil.isValidFileType(images)) {
            throw new IllegalArgumentException("Invalid file type");
        }
        return ResponseEntity.ok(imageService.uploadImages(images, width, height));
    }

}