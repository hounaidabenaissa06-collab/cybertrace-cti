package com.cybertrace.model.ioc;

feature/ioc-security
import com.cybertrace.model.enums.IOCType;
import java.time.LocalDate;
import java.util.*;

public abstract class IOC {
    private String id, value;
    private int confidence;
    private LocalDate firstSeen = LocalDate.now();
    private List<String> tags = new ArrayList<>();

    public IOC(String id, String value, int confidence) {
        this.id=id; this.value=value; this.confidence=confidence;
    }

    public abstract boolean validate();
    public abstract IOCType getType();

    public String toCSVLine() {
        return String.format("%s,%s,%s,%d,%s",
            id, getType(), value, confidence,
            String.join("|", tags));
    }

    public void addTag(String t) { tags.add(t); }
    public String getId(){ return id; }
    public String getValue(){ return value; }
    public int getConfidence(){ return confidence; }

public class IOC {

    private String value;

    public IOC(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
main
}
