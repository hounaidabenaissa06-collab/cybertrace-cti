package com.cybertrace.repository;

import com.cybertrace.model.hunt.HuntMission;
import com.cybertrace.model.hunt.HuntStatus;
import com.cybertrace.security.CryptoUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Repository pour les HuntMissions.
 * Persistance : data/hunts.json.enc
 */
public class HuntRepository implements GenericRepository<HuntMission, String> {

    private static final String FILE = "data/hunts.json.enc";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, HuntMission> cache = new HashMap<>();

    public HuntRepository() { loadFromFile(); }

    @Override
    public void save(HuntMission m) {
        if (m == null) throw new IllegalArgumentException("HuntMission null");
        cache.put(m.getId(), m);
        persistToFile();
    }

    @Override
    public HuntMission findById(String id) { return cache.get(id); }

    @Override
    public List<HuntMission> findAll() { return new ArrayList<>(cache.values()); }

    @Override
    public void update(HuntMission m) {
        if (!cache.containsKey(m.getId()))
            throw new NoSuchElementException("HuntMission introuvable : " + m.getId());
        cache.put(m.getId(), m);
        persistToFile();
    }

    @Override
    public void delete(String id) {
        if (cache.remove(id) == null)
            throw new NoSuchElementException("HuntMission introuvable : " + id);
        persistToFile();
    }

    @Override
    public boolean exists(String id) { return cache.containsKey(id); }

    /**
     * Retourne toutes les missions ayant un statut donné.
     */
    public List<HuntMission> findByStatus(HuntStatus status) {
        List<HuntMission> result = new ArrayList<>();
        for (HuntMission m : cache.values()) {
            if (m.getStatus() == status) result.add(m);
        }
        return result;
    }

    private void persistToFile() {
        try {
            Files.createDirectories(Path.of("data"));
            Files.writeString(Path.of(FILE), CryptoUtils.encrypt(gson.toJson(cache)));
        } catch (Exception e) {
            System.err.println("[ERREUR] Sauvegarde HuntMission échouée : " + e.getMessage());
        }
    }

    private void loadFromFile() {
        Path path = Path.of(FILE);
        if (!Files.exists(path)) return;
        try {
            Type type = new TypeToken<Map<String, HuntMission>>() {}.getType();
            Map<String, HuntMission> loaded = gson.fromJson(
                CryptoUtils.decrypt(Files.readString(path)), type);
            if (loaded != null) cache.putAll(loaded);
        } catch (Exception e) {
            System.err.println("[ERREUR] Chargement HuntMission échoué : " + e.getMessage());
        }
    }
}
