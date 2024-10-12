package com.arasaka.file_uploader.type;

import lombok.Getter;

/**
 * Enum representing valid image types.
 */
@Getter
public enum ImageValidType {
    PNG("image/png"),
    JPEG("image/jpeg");

    private final String type;

    /**
     * Constructor for ImageValidType.
     *
     * @param type the MIME type of the image.
     */
    ImageValidType(String type) {
        this.type = type;
    }
}