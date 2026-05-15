package com.cybertrace.service;

import com.cybertrace.exception.*;
import com.cybertrace.model.threat.*;
import com.cybertrace.model.ioc.*;
import com.cybertrace.model.hunt.Campaign;
import com.cybertrace.model.mitre.Technique;
import com.cybertrace.repository.InMemoryThreatActorRepository;
import com.cybertrace.security.CryptoUtils;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
class IntegrationAPT28Test {

    static ThreatActorService actorService;
    static IOCService         iocService;

    static IOC     iocIp;
    static IOC     iocHash;
    static Campaign campaign;

    @BeforeAll
    static void setup() {
        actorService = new ThreatActorService(new InMemoryThreatActorRepository());
        iocService   = new IOCService();
    }

    @Test @Order(1)
    void step1_createAPT28_shouldBeRegistered() throws Exception {
        APTGroup apt28 = new APTGroup("TA-001", "APT28", "Russie", "GRU");
        actorService.registerActor(apt28);
        ThreatActor found = actorService.getById("TA-001");
        assertNotNull(found);
        assertEquals("APT28", found.getName());
        assertEquals("Russie", found.getOrigin());
    }

    @Test @Order(2)
    void step2_apt28_riskScore_shouldBeHigh() throws Exception {
        ThreatActor found = actorService.getById("TA-001");
        assertTrue(found.getRiskScore() >= 8.0,
            "Score APT28 >= 8.0, obtenu: " + found.getRiskScore());
        assertNotNull(found.getThreatLevel());
    }

    @Test @Order(3)
    void step3_createMaliciousIP_shouldValidate()
            throws DuplicateIOCException, InvalidIOCFormatException {
        iocIp = iocService.createFromInput("IP", "185.220.101.45", 90);
        assertNotNull(iocIp);
        assertTrue(iocIp.validate());
        assertEquals("185.220.101.45", iocIp.getValue());
    }

    @Test @Order(4)
    void step4_createHashSHA256_shouldValidate()
            throws DuplicateIOCException, InvalidIOCFormatException {
        String sha256 = "e3b0c44298fc1c149afbf4c8996fb924"
                      + "27ae41e4649b934ca495991b7852b855";
        iocHash = iocService.createFromInput("HASH", sha256, 95);
        assertNotNull(iocHash);
        assertTrue(iocHash.validate());
    }

    @Test @Order(5)
    void step5_duplicateIP_shouldThrowException() {
        assertThrows(DuplicateIOCException.class, () ->
            iocService.createFromInput("IP", "185.220.101.45", 90));
    }

    @Test @Order(6)
    void step6_invalidIP_shouldThrowException() {
        assertThrows(InvalidIOCFormatException.class, () ->
            iocService.createFromInput("IP", "999.999.999.999", 80));
    }

    @Test @Order(7)
    void step7_createCampaign_withIOCsAndTechniques() {
        campaign = new Campaign("C-001", "Operation Grizzly", "Energie", "TA-001");
        campaign.addIOC(iocIp);
        campaign.addIOC(iocHash);
        campaign.addTechnique(new Technique("T1059", "Command and Scripting Interpreter"));
        campaign.addTechnique(new Technique("T1078", "Valid Accounts"));

        assertEquals("Operation Grizzly", campaign.getName());
        assertEquals("TA-001",            campaign.getActorId());
        assertEquals(2,                   campaign.getIOCs().size());
        assertEquals(2,                   campaign.getTechniques().size());
        assertTrue(campaign.getRiskScore() > 5.0);
    }

    @Test @Order(8)
    void step8_campaign_export_shouldContainKeyInfo() {
        String csv = campaign.exportToCSV();
        String txt = campaign.exportToTXT();
        assertTrue(csv.contains("C-001"));
        assertTrue(csv.contains("Operation Grizzly"));
        assertTrue(txt.contains("Operation Grizzly"));
        assertTrue(txt.contains("Energie"));
        System.out.println("CSV: " + csv);
        System.out.println("TXT:\n"  + txt);
    }

    @Test @Order(9)
    void step9_encryption_shouldHidePlaintext() throws Exception {
        String plain = "APT28-GRU-secret";
        String enc   = CryptoUtils.encrypt(plain);
        assertFalse(enc.contains("APT28"));
        assertNotEquals(plain, enc);
        assertEquals(plain, CryptoUtils.decrypt(enc));
    }

    @Test @Order(10)
    void step10_actor_shouldStillBeRetrievable()
            throws ThreatActorNotFoundException {
        ThreatActor found = actorService.getById("TA-001");
        assertNotNull(found);
        assertEquals("APT28",  found.getName());
        assertEquals("Russie", found.getOrigin());
        System.out.println("\n=== Rapport APT28 ===");
        System.out.println(found.getSummary());
        System.out.printf("Risk: %.1f/10 | Niveau: %s%n",
            found.getRiskScore(), found.getThreatLevel());
    }
}