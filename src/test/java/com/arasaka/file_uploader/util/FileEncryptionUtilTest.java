package com.arasaka.file_uploader.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileEncryptionUtilTest {

    private Cipher cipher;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        cipher = mock(Cipher.class);
        secretKey = mock(SecretKey.class);
    }

    @Test
    void encryptFileToBytes_withValidFile_returnsEncryptedBytes() throws Exception {
        try (MockedStatic<Cipher> mockedCipher = Mockito.mockStatic(Cipher.class);
             MockedStatic<KeyGeneratorUtil> mockedKeyGen = Mockito.mockStatic(KeyGeneratorUtil.class)) {
            mockedCipher.when(() -> Cipher.getInstance("AES")).thenReturn(cipher);
            mockedKeyGen.when(KeyGeneratorUtil::getOrGenerateSecretKey).thenReturn(secretKey);
            when(cipher.doFinal(any(byte[].class))).thenReturn(new byte[]{1, 2, 3});

            byte[] result = FileEncryptionUtil.encryptFileToBytes(new byte[]{4, 5, 6});

            assertNotNull(result);
            assertArrayEquals(new byte[]{1, 2, 3}, result);
        }
    }

    @Test
    void encryptFileToBytes_withEncryptionError_throwsException() throws Exception {
        try (MockedStatic<Cipher> mockedCipher = Mockito.mockStatic(Cipher.class);
             MockedStatic<KeyGeneratorUtil> mockedKeyGen = Mockito.mockStatic(KeyGeneratorUtil.class)) {
            mockedCipher.when(() -> Cipher.getInstance("AES")).thenReturn(cipher);
            mockedKeyGen.when(KeyGeneratorUtil::getOrGenerateSecretKey).thenReturn(secretKey);
            when(cipher.doFinal(any(byte[].class))).thenThrow(new IllegalBlockSizeException());

            assertThrows(IllegalBlockSizeException.class, () -> FileEncryptionUtil.encryptFileToBytes(new byte[]{4, 5, 6}));
        }
    }

    @Test
    void decryptFileFromBytes_withValidEncryptedData_returnsDecryptedBytes() throws Exception {
        try (MockedStatic<Cipher> mockedCipher = Mockito.mockStatic(Cipher.class);
             MockedStatic<KeyGeneratorUtil> mockedKeyGen = Mockito.mockStatic(KeyGeneratorUtil.class)) {
            mockedCipher.when(() -> Cipher.getInstance("AES/ECB/PKCS5Padding")).thenReturn(cipher);
            mockedKeyGen.when(KeyGeneratorUtil::getOrGenerateSecretKey).thenReturn(secretKey);
            when(cipher.doFinal(any(byte[].class))).thenReturn(new byte[]{4, 5, 6});

            byte[] result = FileEncryptionUtil.decryptFileFromBytes(new byte[]{1, 2, 3});

            assertNotNull(result);
            assertArrayEquals(new byte[]{4, 5, 6}, result);
        }
    }

    @Test
    void decryptFileFromBytes_withDecryptionError_throwsException() throws Exception {
        try (MockedStatic<Cipher> mockedCipher = Mockito.mockStatic(Cipher.class);
             MockedStatic<KeyGeneratorUtil> mockedKeyGen = Mockito.mockStatic(KeyGeneratorUtil.class)) {
            mockedCipher.when(() -> Cipher.getInstance("AES/ECB/PKCS5Padding")).thenReturn(cipher);
            mockedKeyGen.when(KeyGeneratorUtil::getOrGenerateSecretKey).thenReturn(secretKey);
            when(cipher.doFinal(any(byte[].class))).thenThrow(new IllegalBlockSizeException("Decryption error"));

            assertThrows(IllegalBlockSizeException.class, () -> FileEncryptionUtil.decryptFileFromBytes(new byte[]{1, 2, 3}));
        }
    }
}