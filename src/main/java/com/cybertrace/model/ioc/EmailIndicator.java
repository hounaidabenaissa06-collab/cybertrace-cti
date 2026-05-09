package com.cybertrace.model.ioc;

import com.cybertrace.model.enums.IOCType;

public class EmailIndicator extends IOC {
    public EmailIndicator(String id, String value, int confidence) {
        super(id, value, confidence);
    }
    @Override
    public boolean validate() {
        return getValue().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }
    @Override public IOCType getType() { return IOCType.EMAIL; }
}
