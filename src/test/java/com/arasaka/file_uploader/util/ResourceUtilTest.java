package com.arasaka.file_uploader.util;

import com.arasaka.file_uploader.domain.ImageEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceUtilTest {

    private ImageEntity imageEntity;
    private List<ImageEntity> imageEntities;

    @BeforeEach
    void setUp() {
        imageEntity = mock(ImageEntity.class);
        imageEntities = List.of(imageEntity);
    }

    @Test
    void ImageEntityToResponseEntity_withValidImageEntity_returnsResponseEntity() {
        try (MockedStatic<FileEncryptionUtil> mockedFileEncryptionUtil = Mockito.mockStatic(FileEncryptionUtil.class);
             MockedStatic<ApacheUtil> mockedApacheUtil = Mockito.mockStatic(ApacheUtil.class)) {
            byte[] decryptedData = new byte[]{1, 2, 3};
            mockedFileEncryptionUtil.when(() -> FileEncryptionUtil.decryptFileFromBytes(any(byte[].class))).thenReturn(decryptedData);
            mockedApacheUtil.when(() -> ApacheUtil.getExtensionFromMimeType(anyString())).thenReturn(".jpg");

            when(imageEntity.getFileName()).thenReturn("image");
            when(imageEntity.getFileType()).thenReturn("image/jpeg");
            when(imageEntity.getFileSize()).thenReturn(3L);
            when(imageEntity.getEncryptedData()).thenReturn(new byte[]{4, 5, 6});

            ResponseEntity<Resource> response = ResourceUtil.ImageEntityToResponseEntity(imageEntity);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertInstanceOf(ByteArrayResource.class, response.getBody());
        }
    }

    @Test
    void ImageEntityToResponseEntity_withInvalidImageEntity_throwsException() {
        try (MockedStatic<FileEncryptionUtil> mockedFileEncryptionUtil = Mockito.mockStatic(FileEncryptionUtil.class)) {
            mockedFileEncryptionUtil.when(() -> FileEncryptionUtil.decryptFileFromBytes(any(byte[].class))).thenThrow(new Exception("Decryption error"));

            when(imageEntity.getEncryptedData()).thenReturn(new byte[]{4, 5, 6});

            assertThrows(Exception.class, () -> ResourceUtil.ImageEntityToResponseEntity(imageEntity));
        }
    }

    @Test
    void ImagesInZipToResponseEntity_withValidImageEntities_returnsResponseEntity() {
        try (MockedStatic<FileEncryptionUtil> mockedFileEncryptionUtil = Mockito.mockStatic(FileEncryptionUtil.class);
             MockedStatic<ApacheUtil> mockedApacheUtil = Mockito.mockStatic(ApacheUtil.class)) {
            byte[] decryptedData = new byte[]{1, 2, 3};
            mockedFileEncryptionUtil.when(() -> FileEncryptionUtil.decryptFileFromBytes(any(byte[].class))).thenReturn(decryptedData);
            mockedApacheUtil.when(() -> ApacheUtil.getExtensionFromMimeType(anyString())).thenReturn(".jpg");

            when(imageEntity.getFileName()).thenReturn("image");
            when(imageEntity.getFileType()).thenReturn("image/jpeg");
            when(imageEntity.getEncryptedData()).thenReturn(new byte[]{4, 5, 6});

            ResponseEntity<Resource> response = ResourceUtil.ImagesInZipToResponseEntity(imageEntities);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertInstanceOf(ByteArrayResource.class, response.getBody());
        }
    }

    @Test
    void ImagesInZipToResponseEntity_withInvalidImageEntities_throwsException() {
        try (MockedStatic<FileEncryptionUtil> mockedFileEncryptionUtil = Mockito.mockStatic(FileEncryptionUtil.class)) {
            mockedFileEncryptionUtil.when(() -> FileEncryptionUtil.decryptFileFromBytes(any(byte[].class))).thenThrow(new Exception("Decryption error"));

            when(imageEntity.getEncryptedData()).thenReturn(new byte[]{4, 5, 6});

            assertThrows(Exception.class, () -> ResourceUtil.ImagesInZipToResponseEntity(imageEntities));
        }
    }
}