package com.arasaka.file_uploader.service;

import com.arasaka.file_uploader.domain.ImageEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling image transactions.
 */
public interface ImageTransactionService {

    /**
     * Saves a list of ImageEntity objects to the repository.
     *
     * @param imageEntities the list of ImageEntity objects to save.
     * @return the list of saved ImageEntity objects.
     */
    List<ImageEntity> saveImages(List<ImageEntity> imageEntities);

    /**
     * Retrieves all ImageEntity objects from the repository.
     *
     * @return a list of all ImageEntity objects.
     */
    List<ImageEntity> getAllImages();

    /**
     * Finds an ImageEntity by its file name.
     *
     * @param fileName the name of the file to find.
     * @return an Optional containing the found ImageEntity, or empty if not found.
     */
    Optional<ImageEntity> findImageByFileName(String fileName);
}