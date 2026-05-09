package com.cybertrace.repository;

import com.cybertrace.model.threat.Campaign;
import com.cybertrace.security.CryptoUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Repository pour les Campaigns.
 * Persistance : data/campaigns.json.enc
 */
public class CampaignRepository implements GenericRepository<Campaign, String> {

    private static final String FILE = "data/campaigns.json.enc";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Campaign> cache = new HashMap<>();

    public CampaignRepository() { loadFromFile(); }

    @Override
    public void save(Campaign c) {
        if (c == null) throw new IllegalArgumentException("Campaign null");
        cache.put(c.getId(), c);
        persistToFile();
    }

    @Override
    public Campaign findById(String id) { return cache.get(id); }

    @Override
    public List<Campaign> findAll() { return new ArrayList<>(cache.values()); }

    @Override
    public void update(Campaign c) {
        if (!cache.containsKey(c.getId()))
            throw new NoSuchElementException("Campaign introuvable : " + c.getId());
        cache.put(c.getId(), c);
        persistToFile();
    }

    @Override
    public void delete(String id) {
        if (cache.remove(id) == null)
            throw new NoSuchElementException("Campaign introuvable : " + id);
        persistToFile();
    }

    @Override
    public boolean exists(String id) { return cache.containsKey(id); }

    /**
     * Recherche des campagnes par secteur cible.
     */
    public List<Campaign> findByTargetSector(String sector) {
        List<Campaign> result = new ArrayList<>();
        for (Campaign c : cache.values()) {
            if (c.getTargetSector() != null &&
                c.getTargetSector().equalsIgnoreCase(sector))
                result.add(c);
        }
        return result;
    }

    private void persistToFile() {
        try {
            Files.createDirectories(Path.of("data"));
            Files.writeString(Path.of(FILE), CryptoUtils.encrypt(gson.toJson(cache)));
        } catch (Exception e) {
            System.err.println("[ERREUR] Sauvegarde Campaign échouée : " + e.getMessage());
        }
    }

    private void loadFromFile() {
        Path path = Path.of(FILE);
        if (!Files.exists(path)) return;
        try {
            Type type = new TypeToken<Map<String, Campaign>>() {}.getType();
            Map<String, Campaign> loaded = gson.fromJson(
                CryptoUtils.decrypt(Files.readString(path)), type);
            if (loaded != null) cache.putAll(loaded);
        } catch (Exception e) {
            System.err.println("[ERREUR] Chargement Campaign échoué : " + e.getMessage());
        }
    }
}
