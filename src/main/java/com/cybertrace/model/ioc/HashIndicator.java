package com.cybertrace.model.ioc;

import com.cybertrace.model.enums.IOCType;

public class HashIndicator extends IOC {
    public HashIndicator(String id, String value, int confidence) {
        super(id, value, confidence);
    }
    @Override
    public boolean validate() {
        return getValue().matches("^[a-fA-F0-9]{32}$") ||
               getValue().matches("^[a-fA-F0-9]{40}$") ||
               getValue().matches("^[a-fA-F0-9]{64}$");
    }
    @Override public IOCType getType() { return IOCType.HASH_SHA256; }
}
