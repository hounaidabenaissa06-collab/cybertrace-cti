package com.cybertrace.model.user;

public class User {

    private String username;

    private String passwordHash;

    private UserRole role;

    public User(String username,
                String passwordHash,
                UserRole role) {

        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }
}