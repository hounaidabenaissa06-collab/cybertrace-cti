package com.cybertrace;

import com.cybertrace.security.EncryptionService;

public class App {

    public static void main(String[] args) {

        try {

            String message = "APT28 detected";

            // Chiffrement
            String encrypted =
                    EncryptionService.encrypt(message);

            System.out.println("Encrypted:");
            System.out.println(encrypted);

            // Déchiffrement
            String decrypted =
                    EncryptionService.decrypt(encrypted);

            System.out.println("Decrypted:");
            System.out.println(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
