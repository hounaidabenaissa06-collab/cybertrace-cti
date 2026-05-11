package com.cybertrace.repository;

import com.cybertrace.model.threat.ThreatActor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation en mémoire (HashMap) — parfaite pour les tests et le jeu.
 * Remplacez par une implémentation JDBC/JPA pour la persistance réelle.
 */
public class InMemoryThreatActorRepository extends ThreatActorRepository {

    // LinkedHashMap pour conserver l'ordre d'insertion
    private final Map<String, ThreatActor> store = new LinkedHashMap<>();

    @Override
    public void save(ThreatActor actor) {
        if (actor == null) throw new IllegalArgumentException("actor ne peut pas être null");
        store.put(actor.getId(), actor);
    }

    @Override
    public ThreatActor findById(String id) {
        return store.get(id);   // retourne null si absent
    }

    @Override
    public List<ThreatActor> findAll() {
        return new ArrayList<>(store.values());  // copie défensive
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }
}