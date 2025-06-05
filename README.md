# Projet MDD - Application Fullstack Java 21 + Angular 19

## Description

Ce projet est une application fullstack composée d'un backend Java 21 avec Spring Boot et d'un frontend Angular. La base de données utilisée est MySQL.

---

## Prérequis

- Java 21 (JDK 21) installé et configuré
- Maven 3.9+ (gestion des dépendances backend)
- Node.js 18+ et npm (gestion du frontend Angular)
- Angular CLI (version compatible avec Angular utilisé)
- MySQL installé et en fonctionnement

---

## Configuration de la base de données

- Nom de la base : `mdd_db`
- Utilisateur : `root`
- Mot de passe : `root`

Crée la base de données MySQL :

```sql
CREATE DATABASE mdd_db;

Installation et lancement du backend (Java 21 + Spring Boot)
	mvn clean install
  mvn spring-boot:run
Le backend sera accessible par défaut sur : http://localhost:8080


Installation et lancement du frontend (Angular)
	1.	Se positionner dans le dossier frontend (ex: ./frontend).
	2.	Installer les dépendances :

npm install

	3.	Lancer l’application Angular :
ng serve --open
Le frontend sera accessible sur : http://localhost:4200

Structure du projet
/backend    # Projet backend Java 21 avec Spring Boot
/frontend   # Projet frontend Angular 19
README.md   # Ce fichier

Tests
	•	Tests backend : exécuter avec Maven
  mvn test

	•	Tests frontend : exécuter avec npm
  npm test

Remarques
	•	Assurez-vous que MySQL tourne avant de lancer le backend.
	•	Pour toute modification de la base, utilise spring.jpa.hibernate.ddl-auto=update ou un outil de migration type Flyway.
Auteur

BALDE Abdourahamane

