package com.cybertrace.interfaces;

/**
 * Interface représentant un objet analysable dans le système CTI.
 * Toute entité pouvant être analysée (ThreatActor, Campaign, HuntMission)
 * doit implémenter cette interface.
 */
public interface Analyzable {

    /**
     * Retourne une analyse textuelle de l'entité.
     * @return chaîne de caractères décrivant l'analyse
     */
    String analyze();

    /**
     * Calcule et retourne le score de risque de l'entité.
     * @return score entre 0.0 et 10.0
     */
    double getRiskScore();
}
