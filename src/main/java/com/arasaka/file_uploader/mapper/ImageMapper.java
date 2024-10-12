package com.arasaka.file_uploader.mapper;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.generated.dto.UploadImageResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper interface for converting ImageEntity objects to UploadImageResponseDto objects.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper {

    /**
     * Converts a list of ImageEntity objects to a list of UploadImageResponseDto objects.
     *
     * @param images the list of ImageEntity objects to convert.
     * @return a list of UploadImageResponseDto objects.
     */
    List<UploadImageResponseDto> imageEntitiesToUploadImageResponseDtos(List<ImageEntity> images);
}