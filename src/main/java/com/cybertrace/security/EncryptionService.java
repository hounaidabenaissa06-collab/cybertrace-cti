package com.cybertrace.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionService {

    // Clé AES de 16 caractères = AES 128 bits
private static final String SECRET_KEY = "cybertracekey123";
    /**
     * Chiffre une chaîne de texte avec AES.
     */
    public static String encrypt(String data) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes =
                cipher.doFinal(data.getBytes());

        return Base64.getEncoder()
                .encodeToString(encryptedBytes);
    }

    /**
     * Déchiffre une chaîne AES.
     */
    public static String decrypt(String encryptedData) throws Exception {

        SecretKeySpec key =
                new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes =
                Base64.getDecoder().decode(encryptedData);

        return new String(cipher.doFinal(decodedBytes));
    }
}
