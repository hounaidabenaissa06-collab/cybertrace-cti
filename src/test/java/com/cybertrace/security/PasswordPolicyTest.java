package com.cybertrace.security;

import com.cybertrace.exception.WeakPasswordException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordPolicyTest {

    @Test
    void validate_validPassword_noException() {
        assertDoesNotThrow(() -> PasswordPolicy.validate("SecurePass1!"),
            "Mot de passe valide ne doit pas lever d'exception");
    }

    @Test
    void validate_tooShort_throwsWeakPasswordException() {
        WeakPasswordException ex = assertThrows(WeakPasswordException.class,
            () -> PasswordPolicy.validate("Short1!"));
        assertTrue(ex.getMessage().contains("10"),
            "Le message doit mentionner la longueur minimale (10)");
    }

    @Test
    void validate_noUppercase_throwsWeakPasswordException() {
        assertThrows(WeakPasswordException.class,
            () -> PasswordPolicy.validate("nouppercase1!longenough"));
    }

    @Test
    void validate_noDigit_throwsWeakPasswordException() {
        assertThrows(WeakPasswordException.class,
            () -> PasswordPolicy.validate("NoDigitsHere!LongEnough"));
    }

    @Test
    void validate_noSymbol_throwsWeakPasswordException() {
        assertThrows(WeakPasswordException.class,
            () -> PasswordPolicy.validate("NoSymbol1234LongEnough"));
    }

    @Test
    void validate_multipleViolations_allReported() {
        WeakPasswordException ex = assertThrows(WeakPasswordException.class,
            () -> PasswordPolicy.validate("abc"));
        assertTrue(ex.getViolations().length >= 3,
            "Plusieurs violations doivent toutes être reportées");
    }
}
