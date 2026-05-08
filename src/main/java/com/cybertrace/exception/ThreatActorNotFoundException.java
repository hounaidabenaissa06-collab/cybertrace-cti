package com.cybertrace.exception;

public class ThreatActorNotFoundException extends Exception {
    public ThreatActorNotFoundException(String id) {
        super("Acteur introuvable avec l'ID: " + id);
    }
}

