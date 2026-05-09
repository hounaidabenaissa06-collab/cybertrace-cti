package com.cybertrace.security;

import java.util.HashMap;
import java.util.Map;

import com.cybertrace.exception.AuthenticationException;
import com.cybertrace.model.user.User;
import com.cybertrace.repository.UserRepository;
import com.cybertrace.utils.HashUtils;

public class AuthService {

    private final UserRepository repo;

    private final Map<String, Integer> failures = new HashMap<>();

    private final Map<String, Boolean> locked = new HashMap<>();

    private User currentUser;

    public AuthService(UserRepository repo) {
        this.repo = repo;
    }

    public User login(String username, String password)
            throws AuthenticationException {

        if (Boolean.TRUE.equals(locked.get(username))) {

            throw new AuthenticationException(
                "Compte bloqué.",
                3
            );
        }

        User u = repo.findByUsername(username);

        if (u == null
                || !HashUtils.verify(password, u.getPasswordHash())) {

            int n = failures.merge(
                username,
                1,
                Integer::sum
            );

            if (n >= 3) {
                locked.put(username, true);
            }

            throw new AuthenticationException(
                "Identifiants invalides. Tentative " + n + "/3",
                n
            );
        }

        failures.remove(username);

        currentUser = u;

        return u;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
