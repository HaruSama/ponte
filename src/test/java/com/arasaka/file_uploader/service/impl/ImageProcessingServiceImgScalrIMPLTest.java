package com.arasaka.file_uploader.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
class ImageProcessingServiceImgScalrIMPLTest {

    @InjectMocks
    private ImageProcessingServiceImgScalrIMPL imageProcessingService;

    @Test
    void resizeImage_withValidImage_returnsResizedImage() throws IOException {
        byte[] originalImageBytes = createTestImage(100, 100);
        byte[] resizedImageBytes = imageProcessingService.resizeImage(originalImageBytes, 50, 50);

        assertNotNull(resizedImageBytes);
        BufferedImage resizedImage = ImageIO.read(new ByteArrayInputStream(resizedImageBytes));
        assertEquals(50, resizedImage.getWidth());
        assertEquals(50, resizedImage.getHeight());
    }

    @Test
    void resizeImage_withInvalidImage_throwsRuntimeException() {
        byte[] invalidImageBytes = new byte[]{0, 1, 2, 3};

        assertThrows(RuntimeException.class, () -> imageProcessingService.resizeImage(invalidImageBytes, 50, 50));
    }

    @Test
    void resizeImage_withNullImage_throwsRuntimeException() {
        assertThrows(RuntimeException.class, () -> imageProcessingService.resizeImage(null, 50, 50));
    }

    private byte[] createTestImage(int width, int height) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        return baos.toByteArray();
    }
}