# CyberTrace — CTI & Threat Hunting Platform

## Description
Plateforme de Cyber Threat Intelligence (CTI) et Threat Hunting développée en Java avec les principes de la Programmation Orientée Objet. Permet de gérer des acteurs de menaces, des indicateurs de compromission (IOC), des campagnes MITRE ATT&CK et des missions de threat hunting.

## Prérequis
- Java 17+ : https://adoptium.net
- Maven 3.8+ : https://maven.apache.org

## Installation et lancement
```bash
git clone https://github.com/votre-groupe/cybertrace-cti.git
cd cybertrace-cti
mvn compile
mvn exec:java -Dexec.mainClass="com.cybertrace.Main"
```

## Utilisateurs par défaut
| Utilisateur | Mot de passe      | Rôle  |
|-------------|-------------------|-------|
| admin       | CyberTrace2026!   | ADMIN |

## Structure des packages
com.cybertrace
├── interfaces/    → Analyzable, Exportable, Searchable, Scorable
├── model/         → ThreatActor, IOC, Campaign, HuntMission, User...
├── repository/    → Persistance JSON chiffrée (AES-128/CBC)
├── service/       → Logique métier (AuthService, IOCService...)
├── security/      → HashUtils (SHA-256 + sel), CryptoUtils (AES)
├── exception/     → Exceptions custom (6 types)
├── utils/         → AuditLogger
└── ui/            → ConsoleUtils, menus

## Persistance et sécurité
- Données stockées dans `data/*.json.enc` (JSON chiffré AES-128/CBC, IV aléatoire)
- Mots de passe hashés SHA-256 avec sel cryptographique
- Blocage de compte après 3 tentatives échouées
- Journal d'audit dans `logs/audit.log`

## Tests unitaires
Suite JUnit 5 — 38 tests, 0 failure

| Classe de test       | Couvre                                | Tests |
|----------------------|---------------------------------------|-------|
| IPIndicatorTest      | Regex IPv4, edge cases                | 8     |
| DomainIndicatorTest  | Regex domaine                         | 6     |
| HashIndicatorTest    | MD5 / SHA1 / SHA256 detection         | 7     |
| EmailIndicatorTest   | Regex email                           | 6     |
| HashUtilsTest        | Sel aléatoire, verify, timing-safe    | 5     |
| CryptoUtilsTest      | Round-trip, IV aléatoire, corruption  | 4     |
| PasswordPolicyTest   | 4 règles métier, violations multiples | 6     |
| IOCServiceTest       | CRUD, DuplicateIOC, InvalidFormat     | 5     |
| AuthServiceTest      | Login, blocage 3 tentatives, logout   | 5     |

```bash
mvn test                     # lancer tous les tests
mvn surefire-report:report   # rapport HTML dans target/site/
```

## Diagramme UML
Voir `docs/uml.png`

## Équipe
- Hounaida Benaissa — Tech Lead & Architecture
- Aya Sidki — Dev Sécurité (IOC, crypto)
- Kaoutar Nabil — Backend & Persistance 
- Ranya Zoubdane — UI & Intégration (console, tests JUnit)

## Lien GitHub
https://github.com/votre-groupe/cybertrace-cti — Tag de livraison : `v1.0.0`
