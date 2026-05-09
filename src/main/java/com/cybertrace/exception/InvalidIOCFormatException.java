package com.cybertrace.exception;

public class InvalidIOCFormatException extends Exception {
    public InvalidIOCFormatException(String v, String expected) {
        super("Format invalide: " + v + " | Attendu: " + expected);
    }
}
