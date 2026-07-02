package com.cybertrace.model;

import com.cybertrace.model.ioc.DomainIndicator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class DomainIndicatorTest {

    @ParameterizedTest(name = "Domaine valide : {0}")
    @ValueSource(strings = {
        "google.com",
        "sub.domain.co.uk",
        "evil-domain.xyz",
        "bestreviews4u.xyz",
        "update-microsoft.ru"
    })
    void validate_shouldReturnTrue_forValidDomains(String d) {
        assertTrue(new DomainIndicator("d1", d, 80).validate());
    }

    @ParameterizedTest(name = "Domaine invalide : {0}")
    @ValueSource(strings = {
        "no-tld",
        ".starts-with-dot.com",
        "-starts-with-dash.com",
        ""
    })
    void validate_shouldReturnFalse_forInvalidDomains(String d) {
        assertFalse(new DomainIndicator("d2", d, 80).validate());
    }
}
