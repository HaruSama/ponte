package com.arasaka.file_uploader.controller;

import com.arasaka.file_uploader.domain.ImageEntity;
import com.arasaka.file_uploader.generated.dto.UploadImageResponseDto;
import com.arasaka.file_uploader.repository.ImageRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("imgscalr")
class ImageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;

    private void saveSampleFile() {
        ImageEntity imageEntity = ImageEntity.builder()
                .fileName("sampleImage")
                .fileType(MediaType.IMAGE_PNG_VALUE)
                .fileSize(new byte[0].length)
                .encryptedData(new byte[0])
                .width(200)
                .height(200)
                .build();
        imageRepository.save(imageEntity);
    }

    @BeforeEach
    void clear() {
        imageRepository.deleteAll();
    }

    @Test
    void downloadAllImages_returnsZipFile() throws Exception {
        saveSampleFile();

        MvcResult result = mockMvc.perform(get("/api/images")
                        .accept("application/zip"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/zip"))
                .andReturn();

        byte[] zipContent = result.getResponse().getContentAsByteArray();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipContent);
             ZipInputStream zis = new ZipInputStream(bais)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                assertNotNull(entry.getName());
                assertEquals("sampleImage.png", entry.getName());
                zis.closeEntry();
            }
        }
    }

    @Test
    void downloadImage_withValidName_returnsImage() throws Exception {
        saveSampleFile();
        mockMvc.perform(get("/api/image/sampleImage")
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
    }

    @Test
    void downloadImage_withInvalidName_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/image/invalidImageName")
                        .accept(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void uploadImages_withValidImages_returnsResponse() throws Exception {
        MockMultipartFile image = new MockMultipartFile("images", "image.jpg", MediaType.IMAGE_JPEG_VALUE, getClass().getResourceAsStream("/test1.jpg"));

        MvcResult result = mockMvc.perform(multipart("/api/images")
                        .file(image)
                        .param("width", "100")
                        .param("height", "100")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        List<UploadImageResponseDto> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertNotNull(response);
    }

    @Test
    void uploadImages_withInvalidFileType_returnsBadRequest() throws Exception {
        MockMultipartFile image = new MockMultipartFile("images", "document.pdf", MediaType.APPLICATION_PDF_VALUE, "pdf content".getBytes());

        mockMvc.perform(multipart("/api/images")
                        .file(image)
                        .param("width", "100")
                        .param("height", "100")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }
}