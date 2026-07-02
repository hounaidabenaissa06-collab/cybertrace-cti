package com.cybertrace.model;

import com.cybertrace.model.ioc.EmailIndicator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class EmailIndicatorTest {

    @ParameterizedTest(name = "Email valide : {0}")
    @ValueSource(strings = {
        "user@example.com",
        "first.last@sub.domain.fr",
        "user+tag@company.io",
        "noreply@update-sec.ru"
    })
    void validate_shouldReturnTrue_forValidEmails(String e) {
        assertTrue(new EmailIndicator("e1", e, 70).validate());
    }

    @ParameterizedTest(name = "Email invalide : {0}")
    @ValueSource(strings = {
        "not-an-email",
        "@no-user.com",
        "missing@",
        "two@@at.com",
        ""
    })
    void validate_shouldReturnFalse_forInvalidEmails(String e) {
        assertFalse(new EmailIndicator("e2", e, 70).validate());
    }
}
