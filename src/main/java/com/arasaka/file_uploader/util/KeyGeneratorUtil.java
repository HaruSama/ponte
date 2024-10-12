package com.arasaka.file_uploader.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for generating and managing AES secret keys.
 */
@Slf4j
public class KeyGeneratorUtil {

    private static final String KEY_FILE_NAME = "secret.key";
    private static final String RESOURCE_DIRECTORY = "src/main/resources/";

    /**
     * Retrieves an existing secret key from the file system or generates a new one if it does not exist.
     *
     * @return the AES secret key.
     * @throws Exception if an error occurs during key generation or retrieval.
     */
    public static SecretKey getOrGenerateSecretKey() throws Exception {
        Path keyFilePath = Paths.get(RESOURCE_DIRECTORY, KEY_FILE_NAME);
        log.info("Checking if secret key exists at path: {}", keyFilePath);

        if (Files.exists(keyFilePath)) {
            log.info("Secret key found, loading from file");
            return loadSecretKey(keyFilePath);
        } else {
            log.info("Secret key not found, generating new key");
            SecretKey secretKey = generateSecretKey();
            saveSecretKey(secretKey, keyFilePath);
            return secretKey;
        }
    }

    /**
     * Generates a new AES secret key.
     *
     * @return the generated AES secret key.
     * @throws Exception if an error occurs during key generation.
     */
    static SecretKey generateSecretKey() throws Exception {
        log.info("Generating new AES secret key");
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        log.trace("AES secret key generated successfully");
        return secretKey;
    }

    /**
     * Saves the given secret key to the specified file path.
     *
     * @param secretKey the secret key to save.
     * @param filePath  the file path where the secret key will be saved.
     * @throws Exception if an error occurs during key saving.
     */
    static void saveSecretKey(SecretKey secretKey, Path filePath) throws Exception {
        log.info("Saving secret key to file: {}", filePath);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, secretKey.getEncoded());
        log.trace("Secret key saved successfully");
    }

    /**
     * Loads a secret key from the specified file path.
     *
     * @param filePath the file path from which to load the secret key.
     * @return the loaded AES secret key.
     * @throws Exception if an error occurs during key loading.
     */
    static SecretKey loadSecretKey(Path filePath) throws Exception {
        log.info("Loading secret key from file: {}", filePath);
        byte[] encodedKey = Files.readAllBytes(filePath);
        SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        log.trace("Secret key loaded successfully");
        return secretKey;
    }
}