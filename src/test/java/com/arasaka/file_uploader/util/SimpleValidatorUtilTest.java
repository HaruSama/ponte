package com.arasaka.file_uploader.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleValidatorUtilTest {

    private MultipartFile image;
    private List<MultipartFile> images;

    @BeforeEach
    void setUp() {
        image = mock(MultipartFile.class);
        images = List.of(image);
    }

    @Test
    void isValidFileType_withValidMimeTypes_returnsTrue() throws Exception {
        try (MockedStatic<ApacheUtil> mockedApacheUtil = Mockito.mockStatic(ApacheUtil.class)) {
            InputStream inputStream = mock(InputStream.class);
            when(image.getInputStream()).thenReturn(inputStream);
            mockedApacheUtil.when(() -> ApacheUtil.getMimeTypeFromInputStream(inputStream)).thenReturn("image/jpeg");

            boolean result = SimpleValidatorUtil.isValidFileType(images);

            assertTrue(result);
        }
    }

    @Test
    void isValidFileType_withInvalidMimeType_returnsFalse() throws Exception {
        try (MockedStatic<ApacheUtil> mockedApacheUtil = Mockito.mockStatic(ApacheUtil.class)) {
            InputStream inputStream = mock(InputStream.class);
            when(image.getInputStream()).thenReturn(inputStream);
            mockedApacheUtil.when(() -> ApacheUtil.getMimeTypeFromInputStream(inputStream)).thenReturn("application/pdf");

            boolean result = SimpleValidatorUtil.isValidFileType(images);

            assertFalse(result);
        }
    }

    @Test
    void isValidFileType_withExceptionDuringValidation_returnsFalse() throws Exception {
        try (MockedStatic<ApacheUtil> mockedApacheUtil = Mockito.mockStatic(ApacheUtil.class)) {
            when(image.getInputStream()).thenThrow(new RuntimeException("Error"));

            boolean result = SimpleValidatorUtil.isValidFileType(images);

            assertFalse(result);
        }
    }

    @Test
    void isValidFileType_withEmptyImageList_returnsTrue() {
        boolean result = SimpleValidatorUtil.isValidFileType(List.of());

        assertTrue(result);
    }
}