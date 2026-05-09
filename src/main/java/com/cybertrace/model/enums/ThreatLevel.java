package com.cybertrace.model.enums;

public enum ThreatLevel {
    LOW("Faible",1), MEDIUM("Moyen",2),
    HIGH("Eleve",3), CRITICAL("Critique",4);
    private final String label;
    private final int score;
    ThreatLevel(String l, int s){ label=l; score=s; }
    public String getLabel(){ return label; }
    public int getScore(){ return score; }
}
