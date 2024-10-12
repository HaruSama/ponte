package com.arasaka.file_uploader.service;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.generated.dto.UploadImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing image-related operations.
 */
public interface ImageService {

    /**
     * Retrieves an ImageEntity by its file name.
     *
     * @param fileName the name of the file to retrieve.
     * @return the ImageEntity associated with the given file name.
     */
    ImageEntity getEntityByName(String fileName);

    /**
     * Downloads all images from the database.
     *
     * @return a list of all ImageEntity objects.
     */
    List<ImageEntity> downloadAllImages();

    /**
     * Uploads and processes a list of images, resizing them to the specified dimensions.
     *
     * @param images the list of MultipartFile objects to upload.
     * @param width  the target width for resizing the images.
     * @param height the target height for resizing the images.
     * @return a list of UploadImageResponseDto objects representing the uploaded images.
     */
    List<UploadImageResponseDto> uploadImages(List<MultipartFile> images, Integer width, Integer height);
}