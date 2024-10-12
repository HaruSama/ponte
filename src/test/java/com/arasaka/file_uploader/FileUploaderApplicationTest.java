package com.arasaka.file_uploader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class FileUploaderApplicationTest {

    @BeforeAll
    static void setup() {
        System.setProperty("spring.profiles.active", "imgscalr");
    }

    @Test
    void applicationStartsSuccessfully() {
        assertDoesNotThrow(() -> SpringApplication.run(FileUploaderApplication.class));
    }
}