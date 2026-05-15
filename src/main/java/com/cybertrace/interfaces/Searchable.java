package com.cybertrace.interfaces;

import java.util.List;

/**
 * Interface générique pour les entités permettant la recherche.
 * Le paramètre de type T représente le type d'objet retourné par les recherches.
* Exemple : {@code Searchable<ThreatActor>} pour rechercher des acteurs.
 */
public interface Searchable<T> {

    /**
     * Recherche des entités par requête textuelle générique.
     * @param query texte de recherche
     * @return liste des entités correspondantes
     */
    List<T> search(String query);

    /**
     * Filtre les entités selon un champ et une valeur précis.
     * @param field  nom du champ (ex: "origin", "type")
     * @param value  valeur attendue du champ
     * @return liste filtrée
     */
    List<T> filter(String field, String value);
}
