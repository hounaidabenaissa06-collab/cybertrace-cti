package com.cybertrace.exception;

public class AuthenticationException extends Exception {

    private int attempts;

    public AuthenticationException(String message, int attempts) {

        super(message);

        this.attempts = attempts;
    }

    public int getAttempts() {
        return attempts;
    }
}
