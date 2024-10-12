package com.arasaka.file_uploader.util;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ApacheUtilTest {

    private MimeTypes mimeTypes;

    @BeforeEach
    void setUp() {
        mimeTypes = mock(MimeTypes.class);
    }

    @Test
    void getExtensionFromMimeType_withValidMimeType_returnsExtension() throws Exception {
        try (MockedStatic<MimeTypes> mockedMimeTypes = Mockito.mockStatic(MimeTypes.class)) {
            mockedMimeTypes.when(MimeTypes::getDefaultMimeTypes).thenReturn(mimeTypes);
            MimeType mimeType = mock(MimeType.class);
            when(mimeTypes.forName("image/jpeg")).thenReturn(mimeType);
            when(mimeType.getExtension()).thenReturn(".jpg");

            String extension = ApacheUtil.getExtensionFromMimeType("image/jpeg");

            assertEquals(".jpg", extension);
        }
    }

    @Test
    void getExtensionFromMimeType_withInvalidMimeType_throwsException() {
        try (MockedStatic<MimeTypes> mockedMimeTypes = Mockito.mockStatic(MimeTypes.class)) {
            mockedMimeTypes.when(MimeTypes::getDefaultMimeTypes).thenReturn(mimeTypes);
            when(mimeTypes.forName("invalid/mime")).thenThrow(new MimeTypeException("Invalid MIME type"));

            assertThrows(MimeTypeException.class, () -> ApacheUtil.getExtensionFromMimeType("invalid/mime"));
        } catch (MimeTypeException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getMimeTypeFromInputStream_withValidInputStream_returnsMimeType() {
        try (MockedConstruction<Tika> mockedTika = Mockito.mockConstruction(Tika.class, (mock, context) -> when(mock.detect(any(InputStream.class))).thenReturn("image/jpeg"))) {
            InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});

            String mimeType = ApacheUtil.getMimeTypeFromInputStream(inputStream);

            assertEquals("image/jpeg", mimeType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}