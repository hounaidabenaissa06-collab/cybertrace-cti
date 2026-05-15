package com.cybertrace.model.hunt;

import java.util.ArrayList;
import java.util.List;

import com.cybertrace.interfaces.Analyzable;
import com.cybertrace.interfaces.Exportable;
import com.cybertrace.model.ioc.IOC;
import com.cybertrace.model.mitre.Technique;

public class Campaign implements Analyzable, Exportable {

    private String id;
    private String name;
    private String targetSector;
    private String actorId;

    private List<IOC> iocs = new ArrayList<>();
    private List<Technique> techniques = new ArrayList<>();

    private CampaignStatus status = CampaignStatus.ACTIVE;

    public Campaign(String id, String name,
                    String targetSector, String actorId) {

        this.id = id;
        this.name = name;
        this.targetSector = targetSector;
        this.actorId = actorId;
    }

    @Override
    public double getRiskScore() {

        double base = 5.0;

        base += iocs.size() * 0.3;
        base += techniques.size() * 0.5;

        return Math.min(base, 10.0);
    }

    @Override
    public String analyze() {

        return String.format(
            "Campagne %s: %d IOCs, %d techniques, secteur: %s",
            name,
            iocs.size(),
            techniques.size(),
            targetSector
        );
    }

    @Override
    public String exportToCSV() {

        return String.format(
            "%s,%s,%s,%s",
            id,
            name,
            actorId,
            status
        );
    }

    @Override
    public String exportToTXT() {

        return "=== " + name + " ===\n"
                + "Acteur: " + actorId + "\n"
                + "Secteur: " + targetSector;
    }

    public void addIOC(IOC ioc) {
        iocs.add(ioc);
    }

    public void addTechnique(Technique technique) {
        techniques.add(technique);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CampaignStatus getStatus() {
        return status;
    }
    public String getActorId()            { return actorId; }
    public List<IOC> getIOCs()            { return iocs; }
    public List<Technique> getTechniques(){ return techniques; }
}