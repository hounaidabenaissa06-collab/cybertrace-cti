package com.cybertrace.repository;

import com.cybertrace.model.threat.ThreatActor;
import java.util.*;

public class ThreatActorRepository {
    private Map<String, ThreatActor> store = new HashMap<>();

    public void save(ThreatActor actor) {
        store.put(actor.getId(), actor);
    }

    public ThreatActor findById(String id) {
        return store.get(id);
    }

    public List<ThreatActor> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean exists(String id) {
        return store.containsKey(id);
    }

    public void update(ThreatActor actor) {
        store.put(actor.getId(), actor);
    }

    public void delete(String id) {
        store.remove(id);
    }
}