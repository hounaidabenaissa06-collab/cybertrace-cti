package com.cybertrace.service;

import com.cybertrace.exception.DuplicateIOCException;
import com.cybertrace.exception.InvalidIOCFormatException;
import com.cybertrace.model.enums.IOCType;
import com.cybertrace.model.ioc.IOC;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration IOCService.
 * Utilise IOCService directement (stockage en mémoire interne).
 */
class IOCServiceTest {

    private IOCService service;

    @BeforeEach
    void setUp() {
        service = new IOCService();
    }

    @Test
    void createFromInput_validIP_shouldSucceed()
            throws DuplicateIOCException, InvalidIOCFormatException {
        IOC ioc = service.createFromInput("IP", "192.168.1.100", 85);
        assertNotNull(ioc);
        assertEquals(IOCType.IP, ioc.getType());
        assertEquals(85, ioc.getConfidence());
        assertEquals("192.168.1.100", ioc.getValue());
    }

    @Test
    void createFromInput_invalidIP_shouldThrowInvalidIOCFormatException() {
        assertThrows(InvalidIOCFormatException.class,
            () -> service.createFromInput("IP", "999.999.999.999", 80));
    }

    @Test
    void createFromInput_duplicateValue_shouldThrowDuplicateIOCException()
            throws Exception {
        service.createFromInput("IP", "10.0.0.1", 90);
        assertThrows(DuplicateIOCException.class,
            () -> service.createFromInput("IP", "10.0.0.1", 70),
            "Un doublon IOC doit lever DuplicateIOCException");
    }

    @Test
    void createFromInput_unknownType_shouldThrowIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
            () -> service.createFromInput("UNKNOWN", "test", 50));
    }

    @Test
    void createAllFourTypes_shouldAllSucceed() {
        assertDoesNotThrow(() -> {
            service.createFromInput("IP",     "1.2.3.4",              90);
            service.createFromInput("DOMAIN", "evil.xyz",              80);
            service.createFromInput("HASH",
                "a".repeat(64),                                         95);
            service.createFromInput("EMAIL",  "attacker@domain.com",  70);
        });
        assertEquals(4, service.getAll().size());
    }
}
