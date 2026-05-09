package com.cybertrace.repository;

import com.cybertrace.model.ioc.IOC;
import com.cybertrace.security.CryptoUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Repository pour les IOC (Indicators of Compromise).
 * Persistance : data/iocs.json.enc
 * Indexation double : par ID et par valeur (pour détecter les doublons).
 */
public class IOCRepository implements GenericRepository<IOC, String> {

    private static final String FILE = "data/iocs.json.enc";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, IOC> cacheById    = new HashMap<>();
    private final Map<String, IOC> cacheByValue = new HashMap<>();

    public IOCRepository() {
        loadFromFile();
    }

    @Override
    public void save(IOC ioc) {
        if (ioc == null) throw new IllegalArgumentException("IOC null");
        cacheById.put(ioc.getId(), ioc);
        cacheByValue.put(ioc.getValue(), ioc);
        persistToFile();
    }

    @Override
    public IOC findById(String id) {
        return cacheById.get(id);
    }

    /**
     * Recherche un IOC par sa valeur exacte (adresse IP, hash, domaine...).
     * Utilisé pour la détection de doublons dans IOCService.
     */
    public IOC findByValue(String value) {
        return cacheByValue.get(value);
    }

    @Override
    public List<IOC> findAll() {
        return new ArrayList<>(cacheById.values());
    }

    @Override
    public void update(IOC ioc) {
        if (!cacheById.containsKey(ioc.getId()))
            throw new NoSuchElementException("IOC introuvable : " + ioc.getId());
        // Mise à jour des deux index
        IOC old = cacheById.get(ioc.getId());
        cacheByValue.remove(old.getValue());
        cacheById.put(ioc.getId(), ioc);
        cacheByValue.put(ioc.getValue(), ioc);
        persistToFile();
    }

    @Override
    public void delete(String id) {
        IOC removed = cacheById.remove(id);
        if (removed == null) throw new NoSuchElementException("IOC introuvable : " + id);
        cacheByValue.remove(removed.getValue());
        persistToFile();
    }

    @Override
    public boolean exists(String id) {
        return cacheById.containsKey(id);
    }

    /**
     * Vérifie si un IOC avec cette valeur existe déjà.
     * Méthode appelée par IOCService pour la détection de doublons.
     */
    public boolean existsByValue(String value) {
        return cacheByValue.containsKey(value);
    }

    private void persistToFile() {
        try {
            Files.createDirectories(Path.of("data"));
            String json = gson.toJson(cacheById);
            Files.writeString(Path.of(FILE), CryptoUtils.encrypt(json));
        } catch (Exception e) {
            System.err.println("[ERREUR] Sauvegarde IOC échouée : " + e.getMessage());
        }
    }

    private void loadFromFile() {
        Path path = Path.of(FILE);
        if (!Files.exists(path)) return;
        try {
            String json = CryptoUtils.decrypt(Files.readString(path));
            Type type = new TypeToken<Map<String, IOC>>() {}.getType();
            Map<String, IOC> loaded = gson.fromJson(json, type);
            if (loaded != null) {
                cacheById.putAll(loaded);
                // Reconstruire l'index par valeur
                for (IOC ioc : loaded.values()) {
                    cacheByValue.put(ioc.getValue(), ioc);
                }
            }
        } catch (Exception e) {
            System.err.println("[ERREUR] Chargement IOC échoué : " + e.getMessage());
        }
    }
}
