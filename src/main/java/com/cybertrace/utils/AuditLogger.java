package com.cybertrace.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger d'audit pour tracer toutes les actions sensibles du système.
 * Écrit dans logs/audit.log (mode APPEND = ne jamais écraser le fichier).
 *
 * Format : [2026-04-25 14:32:01][admin][CREATE_IOC][192.168.1.1][SUCCESS]
 */
public class AuditLogger {

    private static final String LOG_FILE = "logs/audit.log";
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Enregistre une entrée d'audit.
     *
     * @param user     nom de l'utilisateur effectuant l'action
     * @param action   code de l'action (ex: "CREATE_IOC", "LOGIN", "DELETE_ACTOR")
     * @param resource ressource concernée (ex: IP de l'IOC, ID de l'acteur)
     * @param success  true si l'action a réussi, false sinon
     * @throws IOException si l'écriture dans le fichier échoue
     */
    public void log(String user, String action,
                    String resource, boolean success) throws IOException {
        // S'assurer que le dossier logs/ existe
        Files.createDirectories(Path.of("logs"));

        String entry = String.format("[%s][%s][%s][%s][%s]%n",
            LocalDateTime.now().format(FORMATTER),
            user,
            action,
            resource,
            success ? "SUCCESS" : "FAIL");

        Files.writeString(
            Path.of(LOG_FILE),
            entry,
            StandardOpenOption.APPEND,   // Ne pas écraser
            StandardOpenOption.CREATE    // Créer si absent
        );
    }

    /**
     * Variante sans IOException pour les appels non critiques.
     * Avale silencieusement les erreurs I/O (usage : contexte non-critique).
     */
    public void logSilently(String user, String action,
                            String resource, boolean success) {
        try {
            log(user, action, resource, success);
        } catch (IOException e) {
            System.err.println("[WARNING] AuditLog échoué : " + e.getMessage());
        }
    }
}
