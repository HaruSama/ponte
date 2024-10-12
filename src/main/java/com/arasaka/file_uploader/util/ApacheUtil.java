package com.arasaka.file_uploader.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypes;

import java.io.InputStream;

/**
 * Utility class for handling MIME type and file extension operations using Apache Tika.
 */
@Slf4j
public class ApacheUtil {

    /**
     * Retrieves the file extension for a given MIME type.
     *
     * @param mimeType the MIME type for which to get the file extension.
     * @return the file extension corresponding to the given MIME type.
     * @throws Exception if an error occurs while retrieving the file extension.
     */
    public static String getExtensionFromMimeType(String mimeType) throws Exception {
        log.info("Getting extension for MIME type: {}", mimeType);
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        try {
            String extension = allTypes.forName(mimeType).getExtension();
            log.trace("Extension found: {}", extension);
            return extension;
        } catch (Exception e) {
            log.error("Error getting extension for MIME type: {}", mimeType, e);
            throw e;
        }
    }

    /**
     * Detects the MIME type from an InputStream.
     *
     * @param inputStream the InputStream from which to detect the MIME type.
     * @return the detected MIME type as a String.
     * @throws Exception if an error occurs while detecting the MIME type.
     */
    public static String getMimeTypeFromInputStream(InputStream inputStream) throws Exception {
        log.info("Detecting MIME type from InputStream");
        Tika tika = new Tika();
        try {
            String mimeType = tika.detect(inputStream);
            log.trace("MIME type detected: {}", mimeType);
            return mimeType;
        } catch (Exception e) {
            log.error("Error detecting MIME type from InputStream", e);
            throw e;
        }
    }
}