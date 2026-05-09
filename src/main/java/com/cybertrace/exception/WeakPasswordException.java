package com.cybertrace.exception;

public class WeakPasswordException extends Exception {
    private final String[] violations;
    public WeakPasswordException(String[] v) {
        super("Mot de passe faible: " + String.join(", ", v));
        this.violations = v;
    }
    public String[] getViolations() { return violations; }
}
