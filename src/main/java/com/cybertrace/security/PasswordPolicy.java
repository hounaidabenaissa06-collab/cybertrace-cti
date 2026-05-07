package com.cybertrace.security;

import com.cybertrace.exception.WeakPasswordException;
import java.util.ArrayList;
import java.util.List;

public class PasswordPolicy {
    public static void validate(String p) throws WeakPasswordException {
        List<String> errors = new ArrayList<>();
        if (p.length() < 10)             errors.add("min 10 caracteres");
        if (!p.matches(".*[A-Z].*"))     errors.add("une majuscule");
        if (!p.matches(".*[0-9].*"))     errors.add("un chiffre");
        if (!p.matches(".*[!@#$%^&*].*")) errors.add("un symbole");
        if (!errors.isEmpty())
            throw new WeakPasswordException(errors.toArray(new String[0]));
    }
}
