package com.cybertrace.model.threat;

public class CriminalGroup extends ThreatActor {
    private String specialty;
    public CriminalGroup(String id,
                         String name,
                         String origin,
                        String specialty) {

        super(
            id,
            name,
            origin,
            ThreatMotivation.FINANCIAL
        );
         this.specialty = specialty;
    }

    @Override
    public ThreatLevel getThreatLevel() {

        return getCampaignIds().size() >= 3
                ? ThreatLevel.HIGH
                : ThreatLevel.MEDIUM;
    }

    @Override
    public String getSummary() {

        return String.format(
            "[CRIMINAL] %s | %s | %d campagnes",
            getName(),
            getOrigin(),
            getCampaignIds().size()
        );
    }

    @Override
    public String analyze() {

        return "Cybercriminalité financière et ransomware";
    }

    @Override
    public double getRiskScore() {

        return Math.min(
            6.0 + getCampaignIds().size() * 0.4,
            9.0
        );
    }
}