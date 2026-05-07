package com.cybertrace.model.threat;

import com.cybertrace.interfaces.Analyzable;
import java.util.*;

public abstract class ThreatActor implements Analyzable {
    private String id, name, origin;
    private ThreatMotivation motivation;
    private List<String> campaignIds = new ArrayList<>();

    public ThreatActor(String id, String name,
                       String origin, ThreatMotivation motivation) {
        this.id = id; this.name = name;
        this.origin = origin; this.motivation = motivation;
    }

    public abstract ThreatLevel getThreatLevel();
    public abstract String getSummary();

    public void addCampaign(String id) { campaignIds.add(id); }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getOrigin() { return origin; }
    public ThreatMotivation getMotivation() { return motivation; }
    public List<String> getCampaignIds() { return campaignIds; }
}