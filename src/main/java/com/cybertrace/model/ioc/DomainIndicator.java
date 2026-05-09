package com.cybertrace.model.ioc;

import com.cybertrace.model.enums.IOCType;

public class DomainIndicator extends IOC {
    public DomainIndicator(String id, String value, int confidence) {
        super(id, value, confidence);
    }
    @Override
    public boolean validate() {
        return getValue().matches(
            "^([a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9]?\\.)+[a-zA-Z]{2,}$");
    }
    @Override public IOCType getType() { return IOCType.DOMAIN; }
}
