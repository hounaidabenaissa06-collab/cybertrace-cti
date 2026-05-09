package com.cybertrace.exception;

public class DuplicateIOCException extends Exception {
    public DuplicateIOCException(String v) {
        super("IOC deja existant: " + v);
    }
}
