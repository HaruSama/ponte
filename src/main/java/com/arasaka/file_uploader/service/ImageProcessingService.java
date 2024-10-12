package com.arasaka.file_uploader.service;

import jakarta.validation.constraints.NotNull;

/**
 * Service interface for processing images.
 */
public interface ImageProcessingService {

    /**
     * Resizes an image to the specified width and height.
     *
     * @param file   the byte array of the image file to resize.
     * @param width  the target width of the resized image.
     * @param height the target height of the resized image.
     * @return a byte array of the resized image.
     */
    byte[] resizeImage(byte[] file, @NotNull Integer width, @NotNull Integer height);
}