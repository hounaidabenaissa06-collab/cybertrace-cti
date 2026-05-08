package com.cybertrace.security;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {
    private static final String ALGO  = "AES/CBC/PKCS5Padding";
    private static final int    IV_SZ = 16;
    private static final byte[] KEY   = "CyberTrace2026!!".getBytes();

    public static String encrypt(String data) throws Exception {
        byte[] iv = new byte[IV_SZ];
        new SecureRandom().nextBytes(iv);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE,
               new SecretKeySpec(KEY, "AES"),
               new IvParameterSpec(iv));
        byte[] ciphertext = c.doFinal(data.getBytes("UTF-8"));
        byte[] combined = new byte[IV_SZ + ciphertext.length];
        System.arraycopy(iv,         0, combined, 0,     IV_SZ);
        System.arraycopy(ciphertext, 0, combined, IV_SZ, ciphertext.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String enc) throws Exception {
        byte[] combined = Base64.getDecoder().decode(enc);
        if (combined.length < IV_SZ)
            throw new IllegalArgumentException("Donnees chiffrees corrompues");
        byte[] iv         = new byte[IV_SZ];
        byte[] ciphertext = new byte[combined.length - IV_SZ];
        System.arraycopy(combined, 0,     iv,         0, IV_SZ);
        System.arraycopy(combined, IV_SZ, ciphertext, 0, ciphertext.length);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE,
               new SecretKeySpec(KEY, "AES"),
               new IvParameterSpec(iv));
        return new String(c.doFinal(ciphertext), "UTF-8");
    }
}
