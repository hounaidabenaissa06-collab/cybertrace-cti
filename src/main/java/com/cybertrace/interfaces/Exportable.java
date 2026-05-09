package com.cybertrace.interfaces;

/**
 * Interface pour les entités pouvant être exportées.
 * Implémentée par Campaign et tout objet exportable en rapport.
 */
public interface Exportable {

    /**
     * Exporte l'entité au format CSV (une ligne).
     * @return ligne CSV
     */
    String exportToCSV();

    /**
     * Exporte l'entité au format texte lisible.
     * @return représentation textuelle formatée
     */
    String exportToTXT();
}
