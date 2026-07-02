package com.cybertrace.model;

import com.cybertrace.model.enums.IOCType;
import com.cybertrace.model.ioc.IPIndicator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class IPIndicatorTest {

    @ParameterizedTest(name = "IP valide : {0}")
    @ValueSource(strings = {
        "192.168.1.1",
        "8.8.8.8",
        "0.0.0.0",
        "255.255.255.255",
        "185.220.101.45"
    })
    void validate_shouldReturnTrue_forValidIPs(String ip) {
        assertTrue(new IPIndicator("id-1", ip, 90).validate(),
            "Devrait accepter : " + ip);
    }

    @ParameterizedTest(name = "IP invalide : {0}")
    @ValueSource(strings = {
        "256.1.1.1",
        "192.168.1",
        "192.168.1.1.5",
        "abc.def.ghi.jkl",
        "192.168.1.-1",
        "999.999.999.999"
    })
    void validate_shouldReturnFalse_forInvalidIPs(String ip) {
        assertFalse(new IPIndicator("id-2", ip, 50).validate(),
            "Devrait rejeter : " + ip);
    }

    @Test
    void validate_emptyString_shouldReturnFalse() {
        assertFalse(new IPIndicator("id-3", "", 50).validate());
    }

    @Test
    void getType_shouldReturn_IP() {
        assertEquals(IOCType.IP, new IPIndicator("id-4", "1.2.3.4", 80).getType());
    }
}
