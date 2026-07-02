package com.cybertrace.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    @Test
    void encryptDecrypt_roundTrip_shouldReturnOriginal() throws Exception {
        String original = "Données sensibles CTI : APT28, 185.220.101.45";
        String encrypted = CryptoUtils.encrypt(original);
        String decrypted = CryptoUtils.decrypt(encrypted);
        assertEquals(original, decrypted,
            "Le déchiffrement doit retrouver le texte original");
    }

    @Test
    void encrypt_twiceSameInput_producesDifferentCiphertext() throws Exception {
        String data = "test-data-identique";
        String enc1 = CryptoUtils.encrypt(data);
        String enc2 = CryptoUtils.encrypt(data);
        assertNotEquals(enc1, enc2,
            "IV aléatoire → deux chiffrements du même texte doivent différer");
    }

    @Test
    void decrypt_corruptedData_shouldThrowException() {
        assertThrows(Exception.class,
            () -> CryptoUtils.decrypt("donnéescorrompues!!!"),
            "Données corrompues → doit lever une exception");
    }

    @Test
    void encryptDecrypt_specialCharacters_shouldWork() throws Exception {
        String special = "Données spéciales : APT28-GRU-Russie-2026";
        assertEquals(special, CryptoUtils.decrypt(CryptoUtils.encrypt(special)));
    }
}
