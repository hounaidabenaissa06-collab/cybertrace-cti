package com.cybertrace.security;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.threat.APTGroup;
import com.cybertrace.model.threat.CriminalGroup;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.repository.ThreatActorRepository;

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

    // ════════════════════════════════════════════════════════════════════════
    //  FACTORY — crée le bon type et le persiste
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Crée un acteur du bon sous-type, le sauvegarde et le retourne.
     *
     * @param type   "APT" ou "CRIMINAL" (insensible à la casse)
     * @param name   Nom de l'acteur
     * @param origin Pays / région d'origine
     * @param extra  APT → secteur ciblé | CRIMINAL → modus operandi
     * @return L'acteur persisté
     * @throws IllegalArgumentException si le type est inconnu
     */
    public ThreatActor createActor(String type, String name,
                                   String origin, String extra) {

        // ID unique basé sur le timestamp (suffisant pour un jeu)
        String id = "TA-" + System.currentTimeMillis();

        ThreatActor actor = switch (type.toUpperCase()) {
            case "APT"      -> new APTGroup(id, name, origin, extra);
            case "CRIMINAL" -> new CriminalGroup(id, name, origin, extra);
            default -> throw new IllegalArgumentException(
                    "Type d'acteur inconnu : \"" + type + "\". Types valides : APT, CRIMINAL");
        };

        repo.save(actor);
        return actor;   // déjà le bon objet, pas besoin de re-fetch
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CRUD
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Retourne un acteur par son id.
     *
     * @throws ThreatActorNotFoundException si l'id est inconnu
     */
    public ThreatActor getById(String id) throws ThreatActorNotFoundException {
        ThreatActor actor = repo.findById(id);
        if (actor == null) throw new ThreatActorNotFoundException(id);
        return actor;
    }

    /** Retourne tous les acteurs enregistrés. */
    public List<ThreatActor> getAll() {
        return repo.findAll();
    }

    /**
     * Met à jour les champs modifiables d'un acteur existant.
     *
     * @throws ThreatActorNotFoundException si l'id est inconnu
     */
    public ThreatActor update(String id, String newName,
                              String newOrigin, String newExtra)
            throws ThreatActorNotFoundException {

        ThreatActor actor = getById(id);  // lève l'exception si absent

        if (newName   != null && !newName.isBlank())   actor.setName(newName);
        if (newOrigin != null && !newOrigin.isBlank())  actor.setOrigin(newOrigin);

        repo.save(actor);   // upsert — écrase l'entrée existante
        return actor;
    }

    /**
     * Supprime un acteur par son id.
     * Ne lève pas d'exception si l'id est absent (opération idempotente).
     */
    public void delete(String id) {
        repo.deleteById(id);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RECHERCHE
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Recherche des acteurs dont le nom contient {@code query}
     * (insensible à la casse).
     *
     * @param query Chaîne à rechercher (vide → retourne tout)
     */
    public List<ThreatActor> searchByName(String query) {
        if (query == null || query.isBlank()) return repo.findAll();

        String q = query.toLowerCase();
        return repo.findAll().stream()
                .filter(a -> a.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    
    public List<ThreatActor> filterByType(String type) {
        String t = type.toUpperCase();
        return repo.findAll().stream()
                .filter(a -> a.getType().equals(t))
                .collect(Collectors.toList());
    }
}