// src/test/java/com/cybertrace/ThreatActorServiceTest.java
package com.cybertrace;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.repository.InMemoryThreatActorRepository;
import com.cybertrace.security.ThreatActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ThreatActorServiceTest {

    private ThreatActorService service;

    @BeforeEach
    void setUp() {
        service = new ThreatActorService(new InMemoryThreatActorRepository());
    }

    @Test
    void createActor_APT_shouldSucceed() {
        ThreatActor apt = service.createActor("APT", "APT28", "Russie", "GRU");
        assertNotNull(apt);
        assertEquals("APT28", apt.getName());
    }

    @Test
    void createActor_CRIMINAL_shouldSucceed() {
        ThreatActor crim = service.createActor("CRIMINAL", "LockBit", "Russie", "Ransomware");
        assertNotNull(crim);
        assertEquals("LockBit", crim.getName());
    }

    @Test
    void createActor_unknownType_shouldThrowIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
            () -> service.createActor("HACKER", "X", "Y", "Z"));
    }

    @Test
    void getById_existingActor_shouldReturn() throws ThreatActorNotFoundException {
        ThreatActor apt = service.createActor("APT", "Fancy Bear", "Russie", "GRU");
        ThreatActor found = service.getById(apt.getId());
        assertNotNull(found);
        assertEquals("Fancy Bear", found.getName());
    }

    @Test
    void getById_unknownId_shouldThrowThreatActorNotFoundException() {
        assertThrows(ThreatActorNotFoundException.class,
            () -> service.getById("TA-999999"));
    }

    @Test
    void searchByName_partialMatch_shouldReturnResults() {
        service.createActor("APT", "Fancy Bear", "Russie", "GRU");
        service.createActor("CRIMINAL", "LockBit", "Russie", "Ransomware");
        List<ThreatActor> results = service.searchByName("fancy");
        assertEquals(1, results.size());
        assertEquals("Fancy Bear", results.get(0).getName());
    }

    @Test
    void riskScore_APT_shouldBeAtLeast_8_5() {
        ThreatActor apt = service.createActor("APT", "Test APT", "Inconnu", "GRU");
        assertTrue(apt.getRiskScore() >= 8.5,
            "APT sans campagne doit avoir un score >= 8.5");
    }
}
