package com.arasaka.file_uploader.service.impl;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class ImageTransactionServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageTransactionServiceImpl imageTransactionService;

    @Test
    void saveImages_withValidImages_returnsSavedImages() {
        List<ImageEntity> images = List.of(new ImageEntity());
        when(imageRepository.saveAll(images)).thenReturn(images);

        List<ImageEntity> result = imageTransactionService.saveImages(images);

        assertNotNull(result);
        assertEquals(images, result);
        verify(imageRepository, times(1)).saveAll(images);
    }

    @Test
    void getAllImages_returnsListOfImages() {
        List<ImageEntity> images = List.of(new ImageEntity());
        when(imageRepository.findAll()).thenReturn(images);

        List<ImageEntity> result = imageTransactionService.getAllImages();

        assertNotNull(result);
        assertEquals(images, result);
        verify(imageRepository, times(1)).findAll();
    }

    @Test
    void findImageByFileName_withExistingFileName_returnsImageEntity() {
        String fileName = "testImage";
        ImageEntity imageEntity = new ImageEntity();
        when(imageRepository.findByFileName(fileName)).thenReturn(Optional.of(imageEntity));

        Optional<ImageEntity> result = imageTransactionService.findImageByFileName(fileName);

        assertTrue(result.isPresent());
        assertEquals(imageEntity, result.get());
        verify(imageRepository, times(1)).findByFileName(fileName);
    }

    @Test
    void findImageByFileName_withNonExistingFileName_returnsEmptyOptional() {
        String fileName = "nonExistingImage";
        when(imageRepository.findByFileName(fileName)).thenReturn(Optional.empty());

        Optional<ImageEntity> result = imageTransactionService.findImageByFileName(fileName);

        assertFalse(result.isPresent());
        verify(imageRepository, times(1)).findByFileName(fileName);
    }
}