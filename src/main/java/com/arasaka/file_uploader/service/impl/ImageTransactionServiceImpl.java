package com.arasaka.file_uploader.service.impl;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.repository.ImageRepository;
import com.arasaka.file_uploader.service.ImageTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling image transactions.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ImageTransactionServiceImpl implements ImageTransactionService {

    private final ImageRepository imageRepository;

    /**
     * Saves a list of ImageEntity objects to the repository.
     *
     * @param imageEntities the list of ImageEntity objects to save.
     * @return the list of saved ImageEntity objects.
     */
    @Override
    @Transactional
    public List<ImageEntity> saveImages(List<ImageEntity> imageEntities) {
        log.info("Saving {} images", imageEntities.size());
        List<ImageEntity> savedImages = imageRepository.saveAll(imageEntities);
        log.trace("Images saved successfully");
        return savedImages;
    }

    /**
     * Retrieves all ImageEntity objects from the repository.
     *
     * @return a list of all ImageEntity objects.
     */
    @Override
    public List<ImageEntity> getAllImages() {
        log.info("Fetching all images");
        List<ImageEntity> images = imageRepository.findAll();
        log.trace("Fetched {} images", images.size());
        return images;
    }

    /**
     * Finds an ImageEntity by its file name.
     *
     * @param fileName the name of the file to find.
     * @return an Optional containing the found ImageEntity, or empty if not found.
     */
    @Override
    public Optional<ImageEntity> findImageByFileName(String fileName) {
        log.info("Finding image by file name: {}", fileName);
        Optional<ImageEntity> imageEntity = imageRepository.findByFileName(fileName);
        if (imageEntity.isPresent()) {
            log.trace("Image found: {}", fileName);
        } else {
            log.error("Image not found: {}", fileName);
        }
        return imageEntity;
    }
}