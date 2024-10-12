package com.arasaka.file_uploader.service.impl;

import com.arasaka.file_uploader.service.ImageProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Service implementation for processing images using the ImgScalr library.
 */
@Service
@Slf4j
@Profile("imgscalr")
public class ImageProcessingServiceImgScalrIMPL implements ImageProcessingService {

    /**
     * Resizes an image to the specified width and height.
     *
     * @param file   the byte array of the image file to resize.
     * @param width  the target width of the resized image.
     * @param height the target height of the resized image.
     * @return a byte array of the resized image.
     * @throws RuntimeException if an error occurs during the resizing process.
     */
    @Override
    public byte[] resizeImage(byte[] file, Integer width, Integer height) {
        log.info("Starting image resize process for width: {} and height: {}", width, height);
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(file);
            BufferedImage originalImage = ImageIO.read(bais);
            log.trace("Original image read successfully");

            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, width, height);
            log.trace("Image resized successfully");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            log.trace("Resized image written to output stream successfully");

            log.info("Image resize process completed successfully");
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error resizing image", e);
            throw new RuntimeException("Error resizing image", e);
        }
    }
}