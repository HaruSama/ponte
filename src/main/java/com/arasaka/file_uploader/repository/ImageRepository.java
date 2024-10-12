package com.arasaka.file_uploader.repository;

import com.arasaka.file_uploader.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing ImageEntity data from the database.
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    /**
     * Finds an ImageEntity by its file name.
     *
     * @param fileName the name of the file to find.
     * @return an Optional containing the found ImageEntity, or empty if not found.
     */
    Optional<ImageEntity> findByFileName(String fileName);
}