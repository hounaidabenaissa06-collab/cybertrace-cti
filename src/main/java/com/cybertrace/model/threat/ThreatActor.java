package com.cybertrace.model.threat;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import com.cybertrace.interfaces.Analyzable;

/** Classe abstraite représentant un acteur de menace cyber. */
public abstract class ThreatActor implements Analyzable {

    private String id;
    private String name;
    private String origin;
    private Set<String> tags = new HashSet<>();

    private ThreatMotivation motivation;
    
    private List<String> campaignIds = new ArrayList<>();

    public ThreatActor(String id,
                       String name,
                       String origin,
                       ThreatMotivation motivation) {

        this.id = id;
        this.name = name;
        this.origin = origin;
        this.motivation = motivation;
    }

    public abstract ThreatLevel getThreatLevel();

    public abstract String getSummary();
    public abstract String analyze();
    public abstract double getRiskScore();

    public void addCampaign(String id) {
        campaignIds.add(id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public ThreatMotivation getMotivation() {
        return motivation;
    }

    public List<String> getCampaignIds() {
        return campaignIds;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
    public void addTag(String tag) {
    	tags.add(tag);
}
    public Set<String> getTags() {
    	return tags;
}

}