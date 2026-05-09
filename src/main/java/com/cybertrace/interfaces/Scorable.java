package com.cybertrace.interfaces;

/**
 * Interface pour les entités ayant un score calculable.
 * Complète Analyzable pour les entités nécessitant une catégorisation par score.
 */
public interface Scorable {

    /**
     * Calcule le score numérique de l'entité.
     * @return score calculé (exemple : 0.0 à 10.0)
     */
    double computeScore();

    /**
     * Retourne la catégorie textuelle correspondant au score.
     * Exemple : "LOW", "MEDIUM", "HIGH", "CRITICAL"
     * @return catégorie du score
     */
    String getScoreCategory();
}
