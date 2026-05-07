package com.cybertrace.model.threat;

import java.util.*;

public class CriminalGroup extends ThreatActor {
    private String criminalNetwork;
    private double ransomDemandAvg;
    private List<String> malwareUsed = new ArrayList<>();

    public CriminalGroup(String id, String name,
                         String origin, String criminalNetwork) {
        super(id, name, origin, ThreatMotivation.FINANCIAL);
        this.criminalNetwork = criminalNetwork;
    }

    @Override
    public ThreatLevel getThreatLevel() {
        if (ransomDemandAvg > 1_000_000) return ThreatLevel.CRITICAL;
        if (ransomDemandAvg > 100_000)   return ThreatLevel.HIGH;
        return ThreatLevel.MEDIUM;
    }

    @Override
    public String getSummary() {
        return String.format("[CRIM] %s | %s | Réseau: %s | Rançon moy: $%.0f | %d campagnes",
            getName(), getOrigin(), criminalNetwork,
            ransomDemandAvg, getCampaignIds().size());
    }

    @Override
    public String analyze() {
        return String.format("Cybercriminalité financière — ransomware. Malwares: %s",
            malwareUsed.isEmpty() ? "non documentés" : String.join(", ", malwareUsed));
    }

    @Override
    public double getRiskScore() {
        double score = 6.0 + Math.min(ransomDemandAvg / 1_000_000, 2.5);
        score += getCampaignIds().size() * 0.3;
        return Math.min(score, 10.0);
    }

    public void setRansomDemandAvg(double avg) { this.ransomDemandAvg = avg; }
    public void addMalware(String m) { malwareUsed.add(m); }
    public String getCriminalNetwork() { return criminalNetwork; }
    public double getRansomDemandAvg() { return ransomDemandAvg; }
}