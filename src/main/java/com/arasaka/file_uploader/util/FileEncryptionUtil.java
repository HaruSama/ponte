package com.arasaka.file_uploader.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

/**
 * Utility class for file encryption and decryption using AES algorithm.
 */
@Slf4j
public class FileEncryptionUtil {

    private static final String ALGORITHM = "AES";

    /**
     * Encrypts a file represented as a byte array.
     *
     * @param file the byte array representing the file to be encrypted.
     * @return the encrypted byte array.
     * @throws Exception if an error occurs during encryption.
     */
    public static byte[] encryptFileToBytes(byte[] file) throws Exception {
        log.info("Starting file encryption");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, KeyGeneratorUtil.getOrGenerateSecretKey());

        try {
            byte[] encryptedData = cipher.doFinal(file);
            log.trace("File encryption successful");
            return encryptedData;
        } catch (Exception e) {
            log.error("Error during file encryption", e);
            throw e;
        }
    }

    /**
     * Decrypts a file represented as an encrypted byte array.
     *
     * @param encryptedData the byte array representing the encrypted file.
     * @return the decrypted byte array.
     * @throws Exception if an error occurs during decryption.
     */
    public static byte[] decryptFileFromBytes(byte[] encryptedData) throws Exception {
        log.info("Starting file decryption");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = KeyGeneratorUtil.getOrGenerateSecretKey();
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try {
            byte[] decryptedData = cipher.doFinal(encryptedData);
            log.trace("File decryption successful");
            return decryptedData;
        } catch (Exception e) {
            log.error("Error during file decryption", e);
            throw e;
        }
    }
}