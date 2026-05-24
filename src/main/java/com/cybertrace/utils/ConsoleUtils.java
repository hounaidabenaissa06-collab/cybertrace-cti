package com.cybertrace.utils;

import java.util.Scanner;

/**
 * Utilitaires console réutilisables pour tous les menus.
 * Centralise la lecture des entrées utilisateur et l'affichage formaté.
 */
public class ConsoleUtils {

    private static final Scanner sc = new Scanner(System.in);

    /**
     * Affiche le banner ASCII de l'application au démarrage.
     */
    public static void printBanner() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║   CyberTrace CTI Platform v1.0    ║");
        System.out.println("║   Threat Intelligence & Hunting   ║");
        System.out.println("╚═══════════════════════════════════╝");
    }

    /**
     * Lit une chaîne de caractères depuis la console.
     * @param prompt message affiché avant le curseur
     * @return la saisie de l'utilisateur (sans espaces en début/fin)
     */
    public static String readString(String prompt) {
        System.out.print(prompt + " > ");
        return sc.nextLine().trim();
    }

    /**
     * Lit un entier dans un intervalle [min, max]. Redemande si invalide.
     * @param prompt message affiché
     * @param min    valeur minimale acceptée
     * @param max    valeur maximale acceptée
     * @return entier valide dans [min, max]
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(readString(prompt));
                if (v >= min && v <= max) return v;
                System.out.printf("  Entrez un nombre entre %d et %d%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Nombre invalide, réessayez.");
            }
        }
    }

    /**
     * Demande une confirmation oui/non.
     * @param msg message de confirmation
     * @return true si l'utilisateur répond "o" ou "oui"
     */
    public static boolean confirm(String msg) {
        return readString(msg + " [o/n]").equalsIgnoreCase("o");
    }

    /**
     * Affiche une ligne séparatrice.
     */
    public static void separator() {
        System.out.println("──────────────────────────────────────");
    }

    /**
     * Affiche un message d'erreur formaté.
     * @param msg message d'erreur
     */
    public static void printError(String msg) {
        System.out.println("  [!] " + msg);
    }

    /**
     * Affiche un message de succès formaté.
     * @param msg message de succès
     */
    public static void printSuccess(String msg) {
        System.out.println("  [✓] " + msg);
    }
}
