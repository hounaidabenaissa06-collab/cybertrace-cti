package com.cybertrace.model.ioc;

import com.cybertrace.model.enums.IOCType;

public class IPIndicator extends IOC {
    public IPIndicator(String id, String value, int confidence) {
        super(id, value, confidence);
    }
    @Override
    public boolean validate() {
        if (!getValue().matches("^(\\d{1,3}\\.){3}\\d{1,3}$")) return false;
        for (String p : getValue().split("\\.")) {
            int v = Integer.parseInt(p);
            if (v < 0 || v > 255) return false;
        }
        return true;
    }
    @Override public IOCType getType() { return IOCType.IP; }
}
