package com.cybertrace.security;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.threat.APTGroup;
import com.cybertrace.model.threat.CriminalGroup;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.repository.ThreatActorRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour les ThreatActor.
 *
 * Responsabilités :
 *   • Factory  — crée le bon sous-type selon un paramètre String
 *   • CRUD     — create / read / update / delete
 *   • Recherche — filtrage par nom (insensible à la casse)
 */
public class ThreatActorService {

    private final ThreatActorRepository repo;

    public ThreatActorService(ThreatActorRepository repo) {
        this.repo = repo;
    }

    // =========================================================================
    // FACTORY
    // =========================================================================

    /**
     * Crée un acteur du bon sous-type, le sauvegarde et le retourne.
     *
     * @param type   "APT" ou "CRIMINAL"
     * @param name   Nom
     * @param origin Origine
     * @param extra  APT -> secteur ciblé | Criminal -> modus operandi
     */
    public ThreatActor createActor(String type,
                                   String name,
                                   String origin,
                                   String extra) {

        String id = "TA-" + System.currentTimeMillis();

        ThreatActor actor = switch (type.toUpperCase()) {
            case "APT" ->
                    new APTGroup(id, name, origin, extra);

            case "CRIMINAL" ->
                    new CriminalGroup(id, name, origin, extra);

            default ->
                    throw new IllegalArgumentException(
                            "Type d'acteur inconnu : " + type);
        };

        repo.save(actor);
        return actor;
    }

    // =========================================================================
    // CRUD
    // =========================================================================

    public ThreatActor getById(String id)
            throws ThreatActorNotFoundException {

        ThreatActor actor = repo.findById(id);

        if (actor == null)
            throw new ThreatActorNotFoundException(id);

        return actor;
    }

    public List<ThreatActor> getAll() {
        return repo.findAll();
    }

    public ThreatActor update(String id,
                              String newName,
                              String newOrigin,
                              String newExtra)
            throws ThreatActorNotFoundException {

        ThreatActor actor = getById(id);

        if (newName != null && !newName.isBlank())
            actor.setName(newName);

        if (newOrigin != null && !newOrigin.isBlank())
            actor.setOrigin(newOrigin);

        // Si tes sous-classes possèdent un champ "extra",
        // tu peux l'ajouter ici avec un setter.

        repo.save(actor);

        return actor;
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    // =========================================================================
    // RECHERCHE
    // =========================================================================

    public List<ThreatActor> searchByName(String query) {

        if (query == null || query.isBlank())
            return repo.findAll();

        String q = query.toLowerCase();

        return repo.findAll()
                .stream()
                .filter(a -> a.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    /**
     * Filtre selon le type (APTGroup, CriminalGroup...)
     */
    public List<ThreatActor> filterByType(String type) {

        if (type == null || type.isBlank())
            return repo.findAll();

        return repo.findAll()
                .stream()
                .filter(a -> a.getClass()
                        .getSimpleName()
                        .toUpperCase()
                        .contains(type.toUpperCase()))
                .collect(Collectors.toList());
    }

    // =========================================================================
    // REPORT
    // =========================================================================

    /**
     * Affiche tous les acteurs triés par score de risque décroissant.
     */
    public void printReport() {

        List<ThreatActor> all = repo.findAll()
                .stream()
                .sorted(Comparator.comparingDouble(
                        ThreatActor::getRiskScore).reversed())
                .collect(Collectors.toList());

        System.out.println();
        System.out.println("════════════════════════════════════════════");
        System.out.println(" RAPPORT DES THREAT ACTORS (" + all.size() + ")");
        System.out.println("════════════════════════════════════════════");

        for (ThreatActor actor : all) {

            System.out.printf(
                    "%-40s | Risk : %.1f | Level : %s%n",
                    actor.getSummary(),
                    actor.getRiskScore(),
                    actor.getThreatLevel()
            );
        }

        System.out.println("════════════════════════════════════════════");
        System.out.println();
    }

}
