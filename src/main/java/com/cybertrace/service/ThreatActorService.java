package com.cybertrace.service;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.interfaces.Searchable;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.model.threat.ThreatLevel;
import com.cybertrace.repository.ThreatActorRepository;
import java.util.*;
import java.util.stream.Collectors;

public class ThreatActorService implements Searchable<ThreatActor> {

    private final ThreatActorRepository repository;

    public ThreatActorService(ThreatActorRepository repository) {
        this.repository = repository;
    }

    public void registerActor(ThreatActor actor) throws Exception {
        if (repository.exists(actor.getId()))
            throw new Exception("Acteur déjà existant: " + actor.getId());
        repository.save(actor);
        System.out.println("[+] Acteur enregistré: " + actor.getName());
    }

    public ThreatActor getById(String id) throws ThreatActorNotFoundException {
        ThreatActor actor = repository.findById(id);
        if (actor == null) throw new ThreatActorNotFoundException(id);
        return actor;
    }

    public List<ThreatActor> getAllActors() {
        return repository.findAll();
    }

    public void deleteActor(String id) throws ThreatActorNotFoundException {
        if (!repository.exists(id)) throw new ThreatActorNotFoundException(id);
        repository.delete(id);
        System.out.println("[-] Acteur supprimé: " + id);
    }

    @Override
    public List<ThreatActor> search(String query) {
        return repository.findAll().stream()
            .filter(a -> a.getName().toLowerCase()
                          .contains(query.toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public List<ThreatActor> filter(String field, String value) {
        return repository.findAll().stream()
            .filter(a -> {
                switch (field.toLowerCase()) {
                    case "origin":     return a.getOrigin()
                                               .equalsIgnoreCase(value);
                    case "level":      return a.getThreatLevel().name()
                                               .equalsIgnoreCase(value);
                    case "motivation": return a.getMotivation().name()
                                               .equalsIgnoreCase(value);
                    default: return false;
                }
            })
            .collect(Collectors.toList());
    }

    public ThreatActor getHighestRisk() throws ThreatActorNotFoundException {
        return repository.findAll().stream()
            .max(Comparator.comparingDouble(ThreatActor::getRiskScore))
            .orElseThrow(() -> new ThreatActorNotFoundException("aucun"));
    }

    public List<ThreatActor> getRankedByRisk() {
        return repository.findAll().stream()
            .sorted(Comparator.comparingDouble(
                ThreatActor::getRiskScore).reversed())
            .collect(Collectors.toList());
    }

    public void printReport() {
        List<ThreatActor> all = getRankedByRisk();
        System.out.println("\n══════════════════════════════════════");
        System.out.println("   RAPPORT — ACTEURS (" + all.size() + ")");
        System.out.println("══════════════════════════════════════");
        for (ThreatActor a : all) {
            System.out.printf("  %-40s  Risk: %.1f  Niveau: %s%n",
                a.getSummary(), a.getRiskScore(), a.getThreatLevel());
        }
        System.out.println("══════════════════════════════════════\n");
    }
}
