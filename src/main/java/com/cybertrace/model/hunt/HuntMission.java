package com.cybertrace.model.hunt;

import com.cybertrace.interfaces.Analyzable;
import com.cybertrace.model.enums.HuntStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Mission de Threat Hunting — représente une chasse proactive aux menaces.
 * Une mission part d'une hypothèse, génère des findings et produit un rapport.
 */
public class HuntMission implements Analyzable {

    private final String id;
    private final String hypothesis;
    private final String scope;
    private final String assignedAnalyst;
    private HuntStatus status = HuntStatus.OPEN;
    private final List<String> findings = new ArrayList<>();

    public HuntMission(String id, String hypothesis,
                       String scope, String assignedAnalyst) {
        this.id = id;
        this.hypothesis = hypothesis;
        this.scope = scope;
        this.assignedAnalyst = assignedAnalyst;
    }

    /** Démarre la mission de chasse. */
    public void start() {
        this.status = HuntStatus.RUNNING;
    }

    /** Clôture la mission. */
    public void complete() {
        this.status = HuntStatus.COMPLETED;
    }

    /**
     * Ajoute un résultat (finding) à la mission.
     * @param finding description du finding trouvé
     */
    public void addFinding(String finding) {
        findings.add(finding);
    }

    @Override
    public String analyze() {
        return String.format(
            "Hunt [%s] | Hypothèse: %s | %d findings | Statut: %s",
            id, hypothesis, findings.size(), status
        );
    }

    @Override
    public double getRiskScore() {
        // Score basé sur le nombre de findings confirmés
        if (findings.isEmpty()) return 0.0;
        return Math.min(3.0 + findings.size() * 1.5, 10.0);
    }

    public String getId()               { return id; }
    public String getHypothesis()       { return hypothesis; }
    public String getScope()            { return scope; }
    public String getAssignedAnalyst()  { return assignedAnalyst; }
    public HuntStatus getStatus()       { return status; }
    public List<String> getFindings()   { return findings; }
}
