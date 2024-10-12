package com.arasaka.file_uploader.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing an image file stored in the database.
 */
@Entity
@Table(name = "files", indexes = {
        @Index(name = "idx_file_name", columnList = "fileName"),
        @Index(name = "idx_uploaded_at", columnList = "uploadedAt"),
        @Index(name = "idx_updated_at", columnList = "updatedAt")
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

    /**
     * Unique identifier for the image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the file.
     */
    @Column(nullable = false, unique = true)
    private String fileName;

    /**
     * Type of the file (e.g., image/jpeg).
     */
    @Column(nullable = false)
    private String fileType;

    /**
     * Size of the file in bytes.
     */
    @Column(nullable = false)
    private long fileSize;

    /**
     * Encrypted data of the file.
     */
    @Lob
    @Column(nullable = false)
    private byte[] encryptedData;

    /**
     * Width of the image in pixels.
     */
    @Column(nullable = false)
    private int width;

    /**
     * Height of the image in pixels.
     */
    @Column(nullable = false)
    private int height;

    /**
     * Timestamp when the image was uploaded.
     */
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime uploadedAt;

    /**
     * Timestamp when the image was last updated.
     */
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}