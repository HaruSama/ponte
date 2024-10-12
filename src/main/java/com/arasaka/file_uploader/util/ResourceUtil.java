package com.arasaka.file_uploader.util;

import com.arasaka.file_uploader.domain.ImageEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for handling resource-related operations.
 */
@Slf4j
public class ResourceUtil {

    /**
     * Converts an ImageEntity to a ResponseEntity containing the decrypted image as a resource.
     *
     * @param imageEntity the ImageEntity to convert.
     * @return a ResponseEntity containing the decrypted image as a resource.
     */
    @SneakyThrows
    public static ResponseEntity<Resource> ImageEntityToResponseEntity(ImageEntity imageEntity) {
        log.info("Converting ImageEntity to ResponseEntity for image: {}", imageEntity.getFileName());

        ByteArrayResource resource = new ByteArrayResource(FileEncryptionUtil.decryptFileFromBytes(imageEntity.getEncryptedData()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imageEntity.getFileType()));
        headers.setContentLength(imageEntity.getFileSize());
        headers.setContentDispositionFormData("attachment", imageEntity.getFileName() + ApacheUtil.getExtensionFromMimeType(imageEntity.getFileType()));

        log.trace("ImageEntity converted to ResponseEntity successfully");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * Converts a list of ImageEntities to a ResponseEntity containing the images in a ZIP file.
     *
     * @param imageEntities the list of ImageEntities to convert.
     * @return a ResponseEntity containing the images in a ZIP file.
     */
    @SneakyThrows
    public static ResponseEntity<Resource> ImagesInZipToResponseEntity(List<ImageEntity> imageEntities) {
        log.info("Converting list of ImageEntities to ResponseEntity with ZIP file");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (ImageEntity imageEntity : imageEntities) {
                log.trace("Adding image to ZIP: {}", imageEntity.getFileName());
                ZipEntry zipEntry = new ZipEntry(imageEntity.getFileName() + ApacheUtil.getExtensionFromMimeType(imageEntity.getFileType()));
                zos.putNextEntry(zipEntry);
                zos.write(FileEncryptionUtil.decryptFileFromBytes(imageEntity.getEncryptedData()));
                zos.closeEntry();
            }
        } catch (Exception e) {
            log.error("Error while creating ZIP file", e);
            throw e;
        }

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentDispositionFormData("attachment", "images.zip");
        headers.setContentLength(baos.size());

        log.trace("List of ImageEntities converted to ResponseEntity with ZIP file successfully");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}