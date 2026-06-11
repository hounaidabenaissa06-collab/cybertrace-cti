# CyberTrace CTI - Plateforme de Cyber Threat Intelligence

## Description
CyberTrace CTI est une plateforme académique de Cyber Threat Intelligence et Threat Hunting développée en Java dans le cadre d’un projet de 3ème année. 
Le projet permet de gérer des IOC (Indicators of Compromise), des Threat Actors, des APT Groups, et de simuler des campagnes de menaces en utilisant le référentiel MITRE ATT&CK.

Ce projet est le fruit du travail d’une équipe de 4 étudiants encadrés pédagogiquement.

---

## Fonctionnalités principales

- Gestion d’IOC : IP, Hash, Domain, Email
- Gestion de Threat Actors et APT Groups
- Simulation de campagnes malveillantes
- Visualisation et suivi des IOC
- Tests unitaires pour valider le fonctionnement des services

---

## Architecture

Le projet est structuré en plusieurs couches :
```bash
com.cybertrace
│
├── model/ # Modèles métier (IOC, ThreatActor, APTGroup)
├── service/ # Logique métier
├── repository/ # Stockage en mémoire des entités
├── security/ # Gestion des accès et exceptions
├── exception/ # Exceptions personnalisées
├── interfaces/ # Interfaces et abstractions
└── utils/ # Fonctions utilitaires
```
## Installation et exécution

### Prérequis
- Java 17
- Maven

### Exécution du projet

#### Option 1 
- Ouvrir le projet dans IntelliJ IDEA ou Eclipse
- Lancer la classe principale : `com.cybertrace.App`

#### Option 2 (terminal)

Compiler le projet :
```bash
mvn clean install
```
Lancer les tests :
```bash
mvn test
```
Les tests couvrent :
- IOCService
- AuthService
- ThreatActorService
- Tests d’intégration APT


Auteurs :
Kaoutar Nabil
Hounaida Benaissa
Ranya Zoubdane
Aya Sidki

