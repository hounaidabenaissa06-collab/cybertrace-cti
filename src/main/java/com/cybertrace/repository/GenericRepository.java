package com.cybertrace.repository;

import java.util.List;

/**
 * Interface générique définissant les opérations CRUD standard.
 * T = type de l'entité (ex: ThreatActor)
 * ID = type de l'identifiant (généralement String)
 *
 * Tous les repositories du projet héritent de cette interface.
 */
public interface GenericRepository<T, ID> {

    /**
     * Sauvegarde une nouvelle entité ou met à jour une entité existante.
     * @param entity l'entité à sauvegarder
     */
    void save(T entity);

    /**
     * Recherche une entité par son identifiant unique.
     * @param id identifiant de l'entité
     * @return l'entité trouvée, ou null si inexistante
     */
    T findById(ID id);

    /**
     * Retourne toutes les entités stockées.
     * @return liste de toutes les entités
     */
    List<T> findAll();

    /**
     * Met à jour une entité existante.
     * @param entity l'entité avec les nouvelles valeurs
     */
    void update(T entity);

    /**
     * Supprime une entité par son identifiant.
     * @param id identifiant de l'entité à supprimer
     */
    void delete(ID id);

    /**
     * Vérifie si une entité avec cet identifiant existe.
     * @param id identifiant à vérifier
     * @return true si l'entité existe
     */
    boolean exists(ID id);
}
