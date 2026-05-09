package com.cybertrace.repository;

import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.security.CryptoUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Repository pour les ThreatActors.
 * Persistance : fichier JSON chiffré AES dans data/threat_actors.json.enc
 * Cache mémoire : Map<String, ThreatActor> pour accès O(1)
 */
public class ThreatActorRepository implements GenericRepository<ThreatActor, String> {

    private static final String FILE = "data/threat_actors.json.enc";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, ThreatActor> cache = new HashMap<>();

    /**
     * Constructeur : charge les données existantes depuis le fichier chiffré.
     */
    public ThreatActorRepository() {
        loadFromFile();
    }

    @Override
    public void save(ThreatActor actor) {
        if (actor == null || actor.getId() == null)
            throw new IllegalArgumentException("ThreatActor ou ID ne peut pas être null");
        cache.put(actor.getId(), actor);
        persistToFile();
    }

    @Override
    public ThreatActor findById(String id) {
        return cache.get(id);
    }

    @Override
    public List<ThreatActor> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public void update(ThreatActor actor) {
        if (!cache.containsKey(actor.getId()))
            throw new NoSuchElementException("ThreatActor introuvable : " + actor.getId());
        cache.put(actor.getId(), actor);
        persistToFile();
    }

    @Override
    public void delete(String id) {
        if (cache.remove(id) == null)
            throw new NoSuchElementException("ThreatActor introuvable : " + id);
        persistToFile();
    }

    @Override
    public boolean exists(String id) {
        return cache.containsKey(id);
    }

    /**
     * Recherche par nom (insensible à la casse).
     * @param name fragment de nom à chercher
     * @return liste des acteurs dont le nom contient la requête
     */
    public List<ThreatActor> findByName(String name) {
        List<ThreatActor> result = new ArrayList<>();
        for (ThreatActor a : cache.values()) {
            if (a.getName().toLowerCase().contains(name.toLowerCase()))
                result.add(a);
        }
        return result;
    }

    // ────────────────────────────────────────────────────────────────────
    // PERSISTANCE PRIVÉE
    // ────────────────────────────────────────────────────────────────────

    /**
     * Sérialise le cache en JSON, chiffre avec AES, écrit sur disque.
     * Appelée automatiquement après chaque modification (save, update, delete).
     */
    private void persistToFile() {
        try {
            // S'assurer que le dossier data/ existe
            Files.createDirectories(Path.of("data"));

            String json = gson.toJson(cache);
            String encrypted = CryptoUtils.encrypt(json);
            Files.writeString(Path.of(FILE), encrypted);
        } catch (Exception e) {
            System.err.println("[ERREUR] Sauvegarde ThreatActor échouée : " + e.getMessage());
        }
    }

    /**
     * Lit le fichier chiffré, déchiffre, reconstruit le cache Map.
     * Silencieux si le fichier n'existe pas encore (premier démarrage).
     */
    private void loadFromFile() {
        Path path = Path.of(FILE);
        if (!Files.exists(path)) {
            // Premier démarrage : aucun fichier à charger
            return;
        }
        try {
            String encrypted = Files.readString(path);
            String json = CryptoUtils.decrypt(encrypted);
            Type type = new TypeToken<Map<String, ThreatActor>>() {}.getType();
            Map<String, ThreatActor> loaded = gson.fromJson(json, type);
            if (loaded != null) {
                cache.putAll(loaded);
            }
        } catch (Exception e) {
            System.err.println("[ERREUR] Chargement ThreatActor échoué : " + e.getMessage());
        }
    }
}
