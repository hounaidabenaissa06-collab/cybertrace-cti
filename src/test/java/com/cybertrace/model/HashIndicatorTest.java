package com.cybertrace.model;

import com.cybertrace.model.ioc.HashIndicator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class HashIndicatorTest {

    @ParameterizedTest(name = "Hash valide : {0}")
    @ValueSource(strings = {
        "d41d8cd98f00b204e9800998ecf8427e",                                    // MD5 (32)
        "da39a3ee5e6b4b0d3255bfef95601890afd80709",                            // SHA1 (40)
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"    // SHA256 (64)
    })
    void validate_shouldReturnTrue_forValidHashes(String hash) {
        assertTrue(new HashIndicator("h1", hash, 95).validate(),
            "Devrait accepter hash : " + hash.length() + " chars");
    }

    @ParameterizedTest(name = "Hash invalide : {0}")
    @ValueSource(strings = {
        "d41d8cd98f00b204e9800998ecf842",        // MD5 trop court (30)
        "notahash!@#$",                            // caractères non-hexa
        "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ",       // 32 chars mais pas hexa
        ""
    })
    void validate_shouldReturnFalse_forInvalidHashes(String hash) {
        assertFalse(new HashIndicator("h2", hash, 50).validate());
    }
}
