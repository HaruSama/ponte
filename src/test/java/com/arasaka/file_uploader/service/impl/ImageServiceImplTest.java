package com.arasaka.file_uploader.service.impl;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.generated.dto.UploadImageResponseDto;
import com.arasaka.file_uploader.mapper.ImageMapper;
import com.arasaka.file_uploader.service.ImageProcessingService;
import com.arasaka.file_uploader.service.ImageTransactionService;
import com.arasaka.file_uploader.util.ApacheUtil;
import com.arasaka.file_uploader.util.FileEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class ImageServiceImplTest {

    @Mock
    private ImageTransactionService imageTransactionService;

    @Mock
    private ImageProcessingService imageProcessingService;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    void getEntityByName_withExistingFileName_returnsImageEntity() {
        String fileName = "testImage";
        ImageEntity imageEntity = new ImageEntity();
        when(imageTransactionService.findImageByFileName(fileName)).thenReturn(Optional.of(imageEntity));

        ImageEntity result = imageService.getEntityByName(fileName);

        assertNotNull(result);
        assertEquals(imageEntity, result);
        verify(imageTransactionService, times(1)).findImageByFileName(fileName);
    }

    @Test
    void getEntityByName_withNonExistingFileName_throwsIllegalArgumentException() {
        String fileName = "nonExistingImage";
        when(imageTransactionService.findImageByFileName(fileName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> imageService.getEntityByName(fileName));
        verify(imageTransactionService, times(1)).findImageByFileName(fileName);
    }

    @Test
    void downloadAllImages_returnsListOfImageEntities() {
        List<ImageEntity> images = List.of(new ImageEntity());
        when(imageTransactionService.getAllImages()).thenReturn(images);

        List<ImageEntity> result = imageService.downloadAllImages();

        assertNotNull(result);
        assertEquals(images, result);
        verify(imageTransactionService, times(1)).getAllImages();
    }

    @Test
    void uploadImages_withValidImages_returnsUploadImageResponseDtos() {
        List<MultipartFile> images = List.of(new MockMultipartFile("tst", new byte[]{1, 2, 3}));
        byte[] resizedImage = new byte[]{1, 2, 3};

        when(imageProcessingService.resizeImage(any(byte[].class), anyInt(), anyInt())).thenReturn(resizedImage);
        when(imageTransactionService.saveImages(any())).thenReturn(List.of(new ImageEntity()));
        when(imageMapper.imageEntitiesToUploadImageResponseDtos(any())).thenReturn(List.of(new UploadImageResponseDto()));

        try (MockedStatic<ApacheUtil> mockedApacheUtil = Mockito.mockStatic(ApacheUtil.class);
             MockedStatic<FileEncryptionUtil> mockedFileEncryptionUtil = Mockito.mockStatic(FileEncryptionUtil.class)) {

            mockedApacheUtil.when(() -> ApacheUtil.getMimeTypeFromInputStream(any(ByteArrayInputStream.class)))
                    .thenReturn("image/jpeg");

            mockedFileEncryptionUtil.when(() -> FileEncryptionUtil.encryptFileToBytes(any()))
                    .thenReturn(new byte[]{4, 5, 6});

            List<UploadImageResponseDto> result = imageService.uploadImages(images, 100, 100);

            assertNotNull(result);
            assertEquals(1, result.size());

            verify(imageProcessingService, times(1)).resizeImage(any(byte[].class), anyInt(), anyInt());
            verify(imageTransactionService, times(1)).saveImages(any());
        }
    }


    @Test
    void uploadImages_withExceptionDuringProcessing_throwsException() {
        List<MultipartFile> images = List.of(mock(MultipartFile.class));
        when(imageProcessingService.resizeImage(any(), anyInt(), anyInt())).thenThrow(new RuntimeException("Processing error"));

        assertThrows(RuntimeException.class, () -> imageService.uploadImages(images, 100, 100));
        verify(imageProcessingService, times(1)).resizeImage(any(), anyInt(), anyInt());
        verify(imageTransactionService, never()).saveImages(any());
    }

    @Test
    void getFileName_generatesUniqueFileName() {
        when(imageTransactionService.findImageByFileName(anyString())).thenReturn(Optional.empty());

        String fileName = imageService.getFileName();

        assertNotNull(fileName);
        verify(imageTransactionService, times(1)).findImageByFileName(anyString());
    }

    @Test
    void getFileName_withExistingFileName_generatesAnotherUniqueFileName() {
        when(imageTransactionService.findImageByFileName(anyString()))
                .thenReturn(Optional.of(new ImageEntity()))
                .thenReturn(Optional.empty());

        String fileName = imageService.getFileName();

        assertNotNull(fileName);
        verify(imageTransactionService, times(2)).findImageByFileName(anyString());
    }
}