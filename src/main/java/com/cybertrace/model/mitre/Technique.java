package com.cybertrace.model.mitre;

public class Technique {

    private String id;
    private String name;

    public Technique(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}