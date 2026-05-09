package com.cybertrace.service;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.hunt.HuntMission;
import com.cybertrace.model.hunt.HuntResult;
import com.cybertrace.model.hunt.HuntStatus;
import com.cybertrace.repository.HuntRepository;
import com.cybertrace.utils.AuditLogger;

import java.util.List;

/**
 * Service métier pour la gestion des missions de Threat Hunting.
 * Orchestre les opérations entre l'UI (ConsoleUI) et le HuntRepository.
 */
public class HuntService {

    private final HuntRepository repo;
    private final AuditLogger auditLogger;

    /**
     * Constructeur avec injection de dépendances.
     * @param repo        repository des missions hunt
     * @param auditLogger logger pour tracer les actions
     */
    public HuntService(HuntRepository repo, AuditLogger auditLogger) {
        this.repo = repo;
        this.auditLogger = auditLogger;
    }

    /**
     * Crée une nouvelle mission de Threat Hunting.
     *
     * @param hypothesis     hypothèse de départ (ex: "Mouvement latéral via T1078")
     * @param scope          périmètre d'investigation (ex: "Réseau interne segment A")
     * @param analystUsername nom de l'analyste assigné
     * @return la mission créée et sauvegardée
     */
    public HuntMission createMission(String hypothesis,
                                     String scope,
                                     String analystUsername) {
        if (hypothesis == null || hypothesis.isBlank())
            throw new IllegalArgumentException("L'hypothèse ne peut pas être vide");

        String id = "HUNT-" + System.currentTimeMillis();
        HuntMission mission = new HuntMission(id, hypothesis, scope, analystUsername);
        repo.save(mission);
        auditLogger.logSilently(analystUsername, "CREATE_HUNT", id, true);
        return mission;
    }

    /**
     * Démarre une mission (passe de OPEN à RUNNING).
     *
     * @param missionId identifiant de la mission
     * @param user      utilisateur démarrant la mission (pour l'audit)
     * @throws ThreatActorNotFoundException si la mission n'existe pas
     * @throws IllegalStateException        si la mission n'est pas dans l'état OPEN
     */
    public void startMission(String missionId, String user)
            throws ThreatActorNotFoundException {
        HuntMission m = getMissionOrThrow(missionId);
        if (m.getStatus() != HuntStatus.OPEN)
            throw new IllegalStateException(
                "La mission doit être OPEN pour démarrer. Statut actuel : " + m.getStatus());
        m.start();
        repo.update(m);
        auditLogger.logSilently(user, "START_HUNT", missionId, true);
    }

    /**
     * Ajoute un finding (résultat d'investigation) à une mission en cours.
     *
     * @param missionId identifiant de la mission
     * @param result    résultat à ajouter
     * @param user      utilisateur effectuant l'ajout (pour l'audit)
     * @throws ThreatActorNotFoundException si la mission est introuvable
     * @throws IllegalStateException        si la mission n'est pas RUNNING
     */
    public void addFinding(String missionId, HuntResult result, String user)
            throws ThreatActorNotFoundException {
        HuntMission m = getMissionOrThrow(missionId);

        if (m.getStatus() != HuntStatus.RUNNING)
            throw new IllegalStateException(
                "Impossible d'ajouter un finding : la mission doit être RUNNING. " +
                "Statut actuel : " + m.getStatus() +
                ". Utilisez startMission() d'abord.");

        m.addFinding(result);
        repo.update(m);
        auditLogger.logSilently(user, "ADD_FINDING", missionId, true);
    }

    /**
     * Termine une mission (passe à COMPLETED).
     *
     * @param missionId identifiant de la mission
     * @param user      utilisateur terminant la mission
     * @throws ThreatActorNotFoundException si la mission est introuvable
     */
    public void completeMission(String missionId, String user)
            throws ThreatActorNotFoundException {
        HuntMission m = getMissionOrThrow(missionId);
        if (m.getStatus() != HuntStatus.RUNNING)
            throw new IllegalStateException(
                "La mission doit être RUNNING pour être complétée.");
        m.complete();
        repo.update(m);
        auditLogger.logSilently(user, "COMPLETE_HUNT", missionId, true);
    }

    /**
     * Retourne toutes les missions.
     */
    public List<HuntMission> getAllMissions() {
        return repo.findAll();
    }

    /**
     * Retourne les missions par statut.
     */
    public List<HuntMission> getMissionsByStatus(HuntStatus status) {
        return repo.findByStatus(status);
    }

    /**
     * Récupère une mission par ID ou lance une exception.
     */
    public HuntMission getMissionOrThrow(String missionId)
            throws ThreatActorNotFoundException {
        HuntMission m = repo.findById(missionId);
        if (m == null)
            throw new ThreatActorNotFoundException("Mission introuvable : " + missionId);
        return m;
    }
}
