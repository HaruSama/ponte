package com.arasaka.file_uploader.util;

import com.arasaka.file_uploader.type.ImageValidType;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for validating image files.
 */
@Slf4j
public class SimpleValidatorUtil {

    /**
     * Validates the MIME types of a list of image files.
     *
     * @param images the list of image files to validate.
     * @return true if all image files have valid MIME types, false otherwise.
     */
    public static boolean isValidFileType(@NotEmpty List<MultipartFile> images) {
        log.info("Validating MIME types for a list of image files");
        Set<String> validMimeTypes = EnumSet.allOf(ImageValidType.class).stream()
                .map(ImageValidType::getType)
                .collect(Collectors.toSet());
        try {
            for (MultipartFile image : images) {
                String mimeType = ApacheUtil.getMimeTypeFromInputStream(image.getInputStream());
                log.trace("Validating MIME type: {}", mimeType);
                if (!validMimeTypes.contains(mimeType)) {
                    log.warn("Invalid MIME type found: {}", mimeType);
                    return false;
                }
            }
            log.trace("All image files have valid MIME types");
            return true;
        } catch (Exception e) {
            log.error("Error during MIME type validation", e);
            return false;
        }
    }
}