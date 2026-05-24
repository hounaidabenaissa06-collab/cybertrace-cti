package com.cybertrace.repository;

import com.cybertrace.model.threat.ThreatActor;
import java.util.List;

/**
 * Contrat du repository pour ThreatActor.
 * Toute la logique de stockage est dans les sous-classes.
 */
public abstract class ThreatActorRepository {

    public abstract void save(ThreatActor actor);
    public abstract ThreatActor findById(String id);
    public abstract List<ThreatActor> findAll();
    public abstract boolean exists(String id);
    public abstract void update(ThreatActor actor);
    public abstract void delete(String id);
    public abstract void deleteById(String id);
}
