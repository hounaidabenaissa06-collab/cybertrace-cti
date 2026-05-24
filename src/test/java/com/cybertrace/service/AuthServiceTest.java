package com.cybertrace.service;

import com.cybertrace.exception.AuthenticationException;
import com.cybertrace.model.user.User;
import com.cybertrace.model.user.UserRole;
import com.cybertrace.repository.UserRepository;
import com.cybertrace.security.AuthService;
import com.cybertrace.security.HashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        UserRepository userRepo = new UserRepository();
        // Créer un utilisateur admin de test avec mot de passe hashé (avec sel)
        userRepo.save(new User("admin-test",
            HashUtils.hashWithSalt("CyberTrace2026!"),
            UserRole.ADMIN));
        authService = new AuthService(userRepo);
    }

    @Test
    void login_correctCredentials_shouldReturnUser() throws AuthenticationException {
        User u = authService.login("admin-test", "CyberTrace2026!");
        assertNotNull(u);
        assertEquals("admin-test", u.getUsername());
    }

    @Test
    void login_wrongPassword_shouldThrowAuthenticationException() {
        assertThrows(AuthenticationException.class,
            () -> authService.login("admin-test", "mauvaismdp"));
    }

    @Test
    void login_after3Failures_accountShouldBeLocked() {
        // 3 tentatives échouées → compte bloqué
        for (int i = 0; i < 3; i++) {
            assertThrows(AuthenticationException.class,
                () -> authService.login("admin-test", "wrong"));
        }
        // La 4e avec le BON mot de passe doit aussi échouer (compte bloqué)
        AuthenticationException ex = assertThrows(AuthenticationException.class,
            () -> authService.login("admin-test", "CyberTrace2026!"));
        assertTrue(ex.getMessage().toLowerCase().contains("bloqu"),
            "Message doit indiquer que le compte est bloqué");
    }

    @Test
    void logout_shouldSetCurrentUserToNull() throws AuthenticationException {
        authService.login("admin-test", "CyberTrace2026!");
        assertTrue(authService.isLoggedIn());
        authService.logout();
        assertFalse(authService.isLoggedIn());
        assertNull(authService.getCurrentUser());
    }

    @Test
    void login_unknownUser_shouldThrowAuthenticationException() {
        assertThrows(AuthenticationException.class,
            () -> authService.login("userInconnu", "n'importe"));
    }
}
