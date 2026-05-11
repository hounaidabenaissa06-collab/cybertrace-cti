package com.cybertrace;

import com.cybertrace.exception.ThreatActorNotFoundException;
import com.cybertrace.model.threat.ThreatActor;
import com.cybertrace.repository.InMemoryThreatActorRepository;
import com.cybertrace.security.ThreatActorService;

import java.util.List;

/**
 * Mini-test console pour valider Factory + CRUD + Recherche.
 * Lancez main() et vérifiez la sortie.
 */
public class ThreatActorServiceTest {

    public static void main(String[] args) throws Exception {

        ThreatActorService svc =
                new ThreatActorService(new InMemoryThreatActorRepository());

        // ── 1. Factory ────────────────────────────────────────────────────
        System.out.println("=== FACTORY ===");

        ThreatActor apt = svc.createActor("APT", "Fancy Bear", "Russie", "Gouvernement");
        System.out.println("Créé : " + apt);

        ThreatActor crim = svc.createActor("criminal", "LockBit", "Russie", "Ransomware");
        System.out.println("Créé : " + crim);

        // ── 2. Read ───────────────────────────────────────────────────────
        System.out.println("\n=== READ ===");

        ThreatActor found = svc.getById(apt.getId());
        System.out.println("Trouvé par ID : " + found);

        // ── 3. Update ─────────────────────────────────────────────────────
        System.out.println("\n=== UPDATE ===");

        svc.update(apt.getId(), "Fancy Bear v2", null, "Finance, Aérospatial");
        System.out.println("Mis à jour : " + svc.getById(apt.getId()));

        // ── 4. Recherche ──────────────────────────────────────────────────
        System.out.println("\n=== RECHERCHE ===");

        List<ThreatActor> results = svc.searchByName("lock");
        results.forEach(a -> System.out.println("  " + a));

        List<ThreatActor> apts = svc.filterByType("APT");
        System.out.println("Tous les APT : " + apts.size());

        // ── 5. Delete ─────────────────────────────────────────────────────
        System.out.println("\n=== DELETE ===");

        svc.delete(crim.getId());
        System.out.println("Après suppression, total : " + svc.getAll().size());

        // ── 6. Exception ──────────────────────────────────────────────────
        System.out.println("\n=== EXCEPTION ===");
        try {
            svc.getById("TA-999999");
        } catch (ThreatActorNotFoundException e) {
            System.out.println("Capturée : " + e.getMessage());
        }

        // ── 7. Type invalide ──────────────────────────────────────────────
        System.out.println("\n=== TYPE INVALIDE ===");
        try {
            svc.createActor("HACKER", "X", "Y", "Z");
        } catch (IllegalArgumentException e) {
            System.out.println("Capturée : " + e.getMessage());
        }

        System.out.println("\n✅ Tous les tests passent !");
    }
}
