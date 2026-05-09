package com.cybertrace.service;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.hunt.HuntMission;
import com.cybertrace.model.ioc.IOC;
import com.cybertrace.model.threat.Campaign;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.repository.CampaignRepository;
import com.cybertrace.repository.HuntRepository;
import com.cybertrace.repository.IOCRepository;
import com.cybertrace.repository.ThreatActorRepository;

import java.util.List;

/**
 * Service de génération de rapports.
 * Agrège les données de plusieurs repositories pour produire des rapports lisibles.
 */
public class ReportService {

    private final ThreatActorRepository actorRepo;
    private final IOCRepository         iocRepo;
    private final CampaignRepository    campaignRepo;
    private final HuntRepository        huntRepo;

    /**
     * Constructeur avec injection de tous les repositories.
     */
    public ReportService(ThreatActorRepository actorRepo,
                         IOCRepository iocRepo,
                         CampaignRepository campaignRepo,
                         HuntRepository huntRepo) {
        this.actorRepo    = actorRepo;
        this.iocRepo      = iocRepo;
        this.campaignRepo = campaignRepo;
        this.huntRepo     = huntRepo;
    }

    /**
     * Génère un rapport complet pour un Threat Actor.
     *
     * @param actorId identifiant de l'acteur
     * @return rapport formaté en texte
     * @throws ThreatActorNotFoundException si l'acteur est introuvable
     */
    public String generateThreatActorReport(String actorId)
            throws ThreatActorNotFoundException {

        ThreatActor actor = actorRepo.findById(actorId);
        if (actor == null)
            throw new ThreatActorNotFoundException(actorId);

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════╗\n");
        sb.append("║      RAPPORT THREAT ACTOR            ║\n");
        sb.append("╚══════════════════════════════════════╝\n");
        sb.append("Nom      : ").append(actor.getName()).append("\n");
        sb.append("Origine  : ").append(actor.getOrigin()).append("\n");
        sb.append("Motivation : ").append(actor.getMotivation()).append("\n");
        sb.append("Niveau   : ").append(actor.getThreatLevel().getLabel()).append("\n");
        sb.append("Score    : ").append(
            String.format("%.1f/10", actor.getRiskScore())).append("\n");
        sb.append("Analyse  : ").append(actor.analyze()).append("\n");
        sb.append("Campagnes: ").append(actor.getCampaignIds().size()).append("\n");

        return sb.toString();
    }

    /**
     * Génère un rapport de synthèse de toutes les IOCs.
     *
     * @return rapport texte listant toutes les IOCs
     */
    public String generateIOCReport() {
        List<IOC> iocs = iocRepo.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════╗\n");
        sb.append("║         RAPPORT IOC — ").append(
            String.format("%-15s", iocs.size() + " indicateurs")).append("║\n");
        sb.append("╚══════════════════════════════════════╝\n");

        if (iocs.isEmpty()) {
            sb.append("Aucun IOC enregistré.\n");
        } else {
            sb.append(String.format("%-20s %-12s %-5s%n", "Valeur", "Type", "Confiance"));
            sb.append("─".repeat(40)).append("\n");
            for (IOC ioc : iocs) {
                sb.append(String.format("%-20s %-12s %3d%%%n",
                    truncate(ioc.getValue(), 19),
                    ioc.getType(),
                    ioc.getConfidence()));
            }
        }
        return sb.toString();
    }

    /**
     * Génère un rapport pour une mission de Threat Hunting.
     *
     * @param missionId identifiant de la mission
     * @return rapport texte
     * @throws ThreatActorNotFoundException si la mission est introuvable
     */
    public String generateHuntReport(String missionId)
            throws ThreatActorNotFoundException {

        HuntMission m = huntRepo.findById(missionId);
        if (m == null)
            throw new ThreatActorNotFoundException("Mission introuvable : " + missionId);

        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════╗\n");
        sb.append("║       RAPPORT THREAT HUNTING         ║\n");
        sb.append("╚══════════════════════════════════════╝\n");
        sb.append("ID       : ").append(m.getId()).append("\n");
        sb.append("Statut   : ").append(m.getStatus()).append("\n");
        sb.append("Analyste : ").append(m.getAssignedAnalyst()).append("\n");
        sb.append("Analyse  : ").append(m.analyze()).append("\n");
        sb.append("Score    : ").append(
            String.format("%.1f/10", m.getRiskScore())).append("\n");
        return sb.toString();
    }

    /**
     * Génère un rapport global de la plateforme.
     */
    public String generateGlobalSummary() {
        int actors    = actorRepo.findAll().size();
        int iocs      = iocRepo.findAll().size();
        int campaigns = campaignRepo.findAll().size();
        int hunts     = huntRepo.findAll().size();

        return "╔══════════════════════════════════════╗\n" +
               "║       RÉSUMÉ PLATEFORME CTI          ║\n" +
               "╚══════════════════════════════════════╝\n" +
               "Threat Actors  : " + actors    + "\n" +
               "IOC            : " + iocs      + "\n" +
               "Campagnes      : " + campaigns + "\n" +
               "Missions Hunt  : " + hunts     + "\n";
    }

    /** Tronque une chaîne à maxLen caractères avec "…" si nécessaire. */
    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }
}
