package com.cybertrace.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HashUtilsTest {

    @Test
    void hashWithSalt_twoCallsSamePassword_produceDifferentHashes() {
        String h1 = HashUtils.hashWithSalt("MonMotDePasse!");
        String h2 = HashUtils.hashWithSalt("MonMotDePasse!");
        assertNotEquals(h1, h2,
            "Deux hashes du même MDP doivent différer (sel aléatoire différent)");
    }

    @Test
    void verify_shouldReturnTrue_forCorrectPassword() {
        String stored = HashUtils.hashWithSalt("CyberTrace2026!");
        assertTrue(HashUtils.verify("CyberTrace2026!", stored));
    }

    @Test
    void verify_shouldReturnFalse_forWrongPassword() {
        String stored = HashUtils.hashWithSalt("CyberTrace2026!");
        assertFalse(HashUtils.verify("mauvaismdp", stored));
        assertFalse(HashUtils.verify("", stored));
        assertFalse(HashUtils.verify("CyberTrace2026", stored)); // sans !
    }

    @Test
    void verify_shouldReturnFalse_forCorruptedHash() {
        assertFalse(HashUtils.verify("mdp", "format:invalide:trojons"));
        assertFalse(HashUtils.verify("mdp", "pasdedeuxpoints"));
    }

    @Test
    void hash_simple_isDeterministic() {
        String h = HashUtils.hash("test-ioc-value");
        assertEquals(h, HashUtils.hash("test-ioc-value"),
            "hash() simple doit être déterministe (pas de sel)");
        // SHA-256 en Base64 = 44 chars
        assertEquals(44, h.length(),
            "SHA-256 Base64 doit faire 44 caractères");
    }
}
