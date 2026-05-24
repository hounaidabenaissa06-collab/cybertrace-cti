package com.cybertrace.utils;

import com.cybertrace.exception.AuthenticationException;
import com.cybertrace.exception.DuplicateIOCException;
import com.cybertrace.exception.InvalidIOCFormatException;
import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.hunt.Campaign;
import com.cybertrace.model.hunt.HuntMission;
import com.cybertrace.model.ioc.IOC;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.model.user.User;
import com.cybertrace.model.user.UserRole;
import com.cybertrace.repository.InMemoryThreatActorRepository;
import com.cybertrace.security.AuthService;
import com.cybertrace.security.HashUtils;
import com.cybertrace.repository.UserRepository;
import com.cybertrace.security.ThreatActorService;
import com.cybertrace.service.IOCService;

import java.util.List;

/**
 * Menu principal de CyberTrace CTI Platform.
 * Gère le flux d'authentification et la navigation entre les modules.
 */
public class MainMenu {

    private final AuthService authService;
    private final ThreatActorService actorService;
    private final IOCService iocService;

    public MainMenu() {
        // Initialisation du repository et des services
        InMemoryThreatActorRepository actorRepo = new InMemoryThreatActorRepository();
        UserRepository userRepo = new UserRepository();

        // Créer un utilisateur admin par défaut
        userRepo.save(new User("admin",
            HashUtils.hashWithSalt("CyberTrace2026!"),
            UserRole.ADMIN));

        this.authService  = new AuthService(userRepo);
        this.actorService = new ThreatActorService(actorRepo);
        this.iocService   = new IOCService();
    }

    /**
     * Lance l'application : affiche le banner, gère le login,
     * puis boucle sur le menu principal.
     */
    public void start() {
        ConsoleUtils.printBanner();
        handleLogin();
        mainLoop();
    }

    // ── LOGIN ──────────────────────────────────────────────────────────────

    private void handleLogin() {
        System.out.println("\n  Connexion requise");
        ConsoleUtils.separator();
        while (!authService.isLoggedIn()) {
            try {
                String username = ConsoleUtils.readString("Utilisateur");
                String password = ConsoleUtils.readString("Mot de passe");
                User user = authService.login(username, password);
                ConsoleUtils.printSuccess("Bienvenue, " + user.getUsername()
                    + " [" + user.getRole() + "]");
            } catch (AuthenticationException e) {
                ConsoleUtils.printError(e.getMessage());
                if (e.getAttempts() >= 3) {
                    System.out.println("  Compte bloqué. Relancez l'application.");
                    System.exit(1);
                }
            }
        }
    }

    // ── BOUCLE PRINCIPALE ──────────────────────────────────────────────────

    private void mainLoop() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = ConsoleUtils.readInt("Choix", 0, 5);
            switch (choice) {
                case 1 -> threatActorMenu();
                case 2 -> iocMenu();
                case 3 -> huntMenu();
                case 4 -> reportMenu();
                case 5 -> demoMode();
                case 0 -> {
                    authService.logout();
                    System.out.println("\n  Au revoir !");
                    running = false;
                }
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║         MENU PRINCIPAL           ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Threat Actors                ║");
        System.out.println("║  2. IOC Manager                  ║");
        System.out.println("║  3. Threat Hunting               ║");
        System.out.println("║  4. Rapports                     ║");
        System.out.println("║  5. Mode Démo (données APT28)    ║");
        System.out.println("║  0. Déconnexion                  ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    // ── MODULE THREAT ACTORS ───────────────────────────────────────────────

    private void threatActorMenu() {
        ConsoleUtils.separator();
        System.out.println("  MODULE : THREAT ACTORS");
        System.out.println("  1. Créer un acteur  2. Lister tous  3. Rechercher  0. Retour");
        int c = ConsoleUtils.readInt("Choix", 0, 3);
        switch (c) {
            case 1 -> createActor();
            case 2 -> listActors();
            case 3 -> searchActors();
        }
    }

    private void createActor() {
        String type   = ConsoleUtils.readString("Type (APT/CRIMINAL)");
        String name   = ConsoleUtils.readString("Nom");
        String origin = ConsoleUtils.readString("Origine (pays)");
        String extra  = ConsoleUtils.readString("Info complémentaire (sponsor/spécialité)");
        try {
            ThreatActor actor = actorService.createActor(type, name, origin, extra);
            ConsoleUtils.printSuccess("Acteur créé : " + actor.getSummary());
            System.out.printf("  Risk Score : %.1f/10 | Niveau : %s%n",
                actor.getRiskScore(), actor.getThreatLevel());
        } catch (IllegalArgumentException e) {
            ConsoleUtils.printError(e.getMessage());
        }
    }

    private void listActors() {
        List<ThreatActor> actors = actorService.getAll();
        if (actors.isEmpty()) {
            System.out.println("  Aucun acteur enregistré.");
            return;
        }
        System.out.println("\n  === THREAT ACTORS (" + actors.size() + ") ===");
        actors.forEach(a ->
            System.out.printf("  [%s] %s  Risk: %.1f  Niveau: %s%n",
                a.getId(), a.getSummary(),
                a.getRiskScore(), a.getThreatLevel()));
    }

    private void searchActors() {
        String q = ConsoleUtils.readString("Rechercher (nom)");
        List<ThreatActor> results = actorService.searchByName(q);
        if (results.isEmpty()) System.out.println("  Aucun résultat.");
        else results.forEach(a -> System.out.println("  → " + a.getSummary()));
    }

    // ── MODULE IOC ─────────────────────────────────────────────────────────

    private void iocMenu() {
        ConsoleUtils.separator();
        System.out.println("  MODULE : IOC MANAGER");
        System.out.println("  1. Ajouter IOC  2. Lister tous  0. Retour");
        int c = ConsoleUtils.readInt("Choix", 0, 2);
        switch (c) {
            case 1 -> addIOC();
            case 2 -> listIOCs();
        }
    }

    private void addIOC() {
        System.out.println("  Types : IP | DOMAIN | HASH | EMAIL");
        String type  = ConsoleUtils.readString("Type");
        String value = ConsoleUtils.readString("Valeur");
        int conf     = ConsoleUtils.readInt("Confiance (0-100)", 0, 100);
        try {
            IOC ioc = iocService.createFromInput(type, value, conf);
            ConsoleUtils.printSuccess("IOC créé : [" + ioc.getType() + "] " + ioc.getValue()
                + " (confiance: " + ioc.getConfidence() + "%)");
        } catch (DuplicateIOCException e) {
            ConsoleUtils.printError("Doublon détecté : " + e.getMessage());
        } catch (InvalidIOCFormatException e) {
            ConsoleUtils.printError("Format invalide : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            ConsoleUtils.printError(e.getMessage());
        }
    }

    private void listIOCs() {
        List<IOC> iocs = iocService.getAll();
        if (iocs.isEmpty()) {
            System.out.println("  Aucun IOC enregistré.");
            return;
        }
        System.out.println("\n  === IOCs (" + iocs.size() + ") ===");
        iocs.forEach(ioc ->
            System.out.printf("  [%s] %s  Confiance: %d%%%n",
                ioc.getType(), ioc.getValue(), ioc.getConfidence()));
    }

    // ── MODULE HUNT ────────────────────────────────────────────────────────

    private void huntMenu() {
        ConsoleUtils.separator();
        System.out.println("  MODULE : THREAT HUNTING");
        System.out.println("  1. Créer mission  0. Retour");
        int c = ConsoleUtils.readInt("Choix", 0, 1);
        if (c == 1) createHuntMission();
    }

    private void createHuntMission() {
        String id         = "HUNT-" + System.currentTimeMillis();
        String hypothesis = ConsoleUtils.readString("Hypothèse");
        String scope      = ConsoleUtils.readString("Périmètre");
        String analyst    = authService.getCurrentUser().getUsername();

        HuntMission mission = new HuntMission(id, hypothesis, scope, analyst);
        mission.start();
        ConsoleUtils.printSuccess("Mission démarrée : " + mission.analyze());

        while (ConsoleUtils.confirm("Ajouter un finding ?")) {
            String finding = ConsoleUtils.readString("Finding");
            mission.addFinding(finding);
            System.out.printf("  Risk Score actuel : %.1f/10%n", mission.getRiskScore());
        }
        mission.complete();
        ConsoleUtils.printSuccess("Mission clôturée. " + mission.analyze());
    }

    // ── MODULE RAPPORTS ────────────────────────────────────────────────────

    private void reportMenu() {
        ConsoleUtils.separator();
        actorService.printReport();
        System.out.println("\n  IOCs enregistrés : " + iocService.getAll().size());
    }

    // ── MODE DÉMO ─────────────────────────────────────────────────────────

    private void demoMode() {
        System.out.println("\n  === MODE DÉMO : APT28 + Lazarus Group ===");
        try {
            // APT28
            ThreatActor apt28 = actorService.createActor(
                "APT", "APT28", "Russie", "GRU");
            apt28.addCampaign("C-001");
            apt28.addCampaign("C-002");
            ConsoleUtils.printSuccess("APT28 créé → " + apt28.getSummary());
            System.out.printf("  Risk Score: %.1f | Niveau: %s%n",
                apt28.getRiskScore(), apt28.getThreatLevel());

            // Lazarus
            ThreatActor lazarus = actorService.createActor(
                "CRIMINAL", "Lazarus Group", "Corée du Nord", "Cryptomonnaie");
            ConsoleUtils.printSuccess("Lazarus créé → " + lazarus.getSummary());

            // IOCs APT28
            iocService.createFromInput("IP",     "185.220.101.45",         90);
            iocService.createFromInput("DOMAIN", "update-microsoft.ru",    85);
            iocService.createFromInput("HASH",
                "e3b0c44298fc1c149afbf4c8996fb924" +
                "27ae41e4649b934ca495991b7852b855", 95);

            // IOCs Lazarus
            iocService.createFromInput("IP",     "175.45.176.10",          80);
            iocService.createFromInput("DOMAIN", "bestreviews4u.xyz",      70);
            iocService.createFromInput("EMAIL",  "noreply@update-sec.ru",  75);

            ConsoleUtils.printSuccess("6 IOCs créés");

            // Campagne
            Campaign c = new Campaign("C-001", "Operation Grizzly", "Énergie", apt28.getId());
            iocService.getAll().stream()
                .filter(ioc -> ioc.getValue().equals("185.220.101.45"))
                .findFirst().ifPresent(c::addIOC);
            System.out.println("  Campagne : " + c.analyze());
            System.out.println("  Export CSV : " + c.exportToCSV());

            System.out.println("\n  RAPPORT FINAL :");
            actorService.printReport();

        } catch (Exception e) {
            ConsoleUtils.printError("Démo : " + e.getMessage());
        }
    }
}
