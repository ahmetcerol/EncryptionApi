package com.example.encryption.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    private final String base64Key;

    public EncryptionService() throws Exception {
        this.base64Key = generateBase64Key(256);
    }

    public String encrypt(String message) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decrypted);
    }

    private static String generateBase64Key(int keySize) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keySize / 8];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
