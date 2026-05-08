package com.cybertrace.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    public static String hash(String value) {

        try {

            MessageDigest md =
                MessageDigest.getInstance("SHA-256");

            byte[] bytes =
                md.digest(value.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();

            for (byte b : bytes) {

                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String raw,
                                 String hashed) {

        return hash(raw).equals(hashed);
    }
}
