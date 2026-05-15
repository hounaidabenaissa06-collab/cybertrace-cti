package com.cybertrace;

import com.cybertrace.ui.MainMenu;

/**
 * Point d'entrée principal de CyberTrace CTI Platform.
 */
public class App {
    /**
     * Lance l'application CyberTrace.
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        new MainMenu().start();
    }
}