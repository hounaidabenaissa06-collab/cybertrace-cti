package com.cybertrace.model.threat;

import java.util.ArrayList;
import java.util.List;

public class APTGroup extends ThreatActor {

    private String sponsoringState;

    private List<String> targetSectors = new ArrayList<>();

    public APTGroup(String id,
                    String name,
                    String origin,
                    String sponsoringState) {

        super(id, name, origin, ThreatMotivation.ESPIONAGE);

        this.sponsoringState = sponsoringState;
    }

    @Override
    public ThreatLevel getThreatLevel() {

        return getCampaignIds().size() >= 2
                ? ThreatLevel.CRITICAL
                : ThreatLevel.HIGH;
    }

    @Override
    public String getSummary() {

        return String.format(
            "[APT] %s | %s | Sponsor: %s | %d campagnes",
            getName(),
            getOrigin(),
            sponsoringState,
            getCampaignIds().size()
        );
    }

    @Override
    public String analyze() {

        return "Espionnage d'état, cibles stratégiques";
    }

    @Override
    public double getRiskScore() {

        return Math.min(
            8.5 + getCampaignIds().size() * 0.5,
            10.0
        );
    }

    public void addTargetSector(String sector) {
        targetSectors.add(sector);
    }

    public List<String> getTargetSectors() {
        return targetSectors;
    }
}