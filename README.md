# JumpMind

![Static Badge](https://img.shields.io/badge/Java-17%2B-blue)
![Static Badge](https://img.shields.io/badge/Platform-Desktop-lightgrey)
![Static Badge](https://img.shields.io/badge/License-MIT-green)

**JumpMind** est un jeu de plateforme 2D développé en Java avec Swing. Incarnez un héros circulaire capable de courir, sauter, dasher et glisser le long des murs à travers des niveaux remplis de pièges et d'ennemis.

---

## Table des matières

- [Aperçu](#aperçu)
- [Gameplay](#gameplay)
- [Structure du projet](#structure-du-projet)
- [Prérequis](#prérequis)
- [Compilation et exécution](#compilation-et-exécution)
- [Contrôles](#contrôles)
- [Documentation](#documentation)
- [Licence](#licence)

---

## Aperçu

JumpMind est un jeu de plateforme nerveux avec une physique soignée et des mécaniques modernes. Le joueur contrôle une boule lumineuse qui doit traverser 5 niveaux en évitant les pièges et les ennemis pour atteindre le portail de sortie.

**Caractéristiques principales :**
- Moteur physique personnalisé (accélération, friction, gravité)
- Coyote time et jump buffer pour un gameplay indulgent
- Dash directionnel avec cooldown
- Wall slide et wall jump
- Déformation visuelle du personnage (squash & stretch)
- Particules, screen shake et hitstop (game feel)
- Caméra fluide avec lerp
- Système de niveaux basé sur des fichiers texte

---

## Gameplay

Le but est d'atteindre le portail (cercle vert pulsant) à la fin de chaque niveau. Le joueur doit éviter :
- Les **pièges** (triangles rouges) : mort instantanée au contact
- Les **ennemis** (carrés orange) : mort instantanée au contact
- La **chute dans le vide** : mort si le joueur tombe trop bas

Le jeu comporte 5 niveaux. Une fois tous les niveaux complétés, l'écran de victoire s'affiche.

---

## Structure du projet

```
JumpMind/
├── Main.java                  # Point d'entrée
├── README.md                  # Documentation principale
├── sources.txt                # Liste des fichiers sources
├── docs/                      # Documentation détaillée
│   ├── architecture.md        # Architecture globale
│   ├── engine.md              # Moteur de jeu
│   ├── entities.md            # Entités
│   ├── game.md                # Logique de jeu
│   ├── ui.md                  # Interface utilisateur
│   ├── world.md               # Objets du monde
│   ├── levels.md              # Système de niveaux
│   └── api-reference.md       # Référence API
├── engine/                    # Boucle de jeu et entrées
│   ├── GamePanel.java
│   ├── GameLoop.java
│   └── InputHandler.java
├── entities/                  # Entités du jeu
│   ├── Entity.java
│   ├── Hero.java
│   ├── Enemy.java
│   ├── Portal.java
│   └── PlayerState.java
├── game/                      # Logique de jeu
│   ├── Game.java
│   ├── Level.java
│   ├── Camera.java
│   └── Particle.java
├── world/                     # Objets du monde
│   ├── Platform.java
│   ├── Trap.java
│   └── PowerUp.java
├── ui/                        # Interface utilisateur
│   ├── Menu.java
│   ├── HUD.java
│   └── GameOver.java
└── levels/                    # Fichiers de niveaux
    ├── level1.txt
    ├── level2.txt
    └── level3.txt
```

---

## Prérequis

- **Java Development Kit (JDK)** version 17 ou supérieure
- Un terminal ou IDE (IntelliJ, Eclipse, VS Code)

---

## Compilation et exécution

### Compilation

```bash
javac Main.java engine/*.java entities/*.java game/*.java world/*.java ui/*.java
```

### Exécution

```bash
java Main
```

### Compilation et exécution en une commande

```bash
javac Main.java engine/*.java entities/*.java game/*.java world/*.java ui/*.java && java Main
```

---

## Contrôles

| Action       | Touches                               |
|-------------|---------------------------------------|
| Déplacement  | `A` / `D` ou `←` / `→`              |
| Saut         | `Espace`, `W` ou `↑`                 |
| Dash         | `Maj` (Shift)                         |
| Menu / Recommencer | `Entrée`                        |

---

## Documentation

La documentation complète du projet est disponible dans le dossier [`docs/`](docs/). Voici un aperçu de chaque fichier :

| Fichier | Contenu |
|---------|---------|
| [`architecture.md`](docs/architecture.md) | Architecture globale, flux d'exécution, diagramme des packages |
| [`engine.md`](docs/engine.md) | GamePanel, GameLoop (60 FPS), InputHandler |
| [`entities.md`](docs/entities.md) | Entity, Hero, Enemy, Portal, PlayerState |
| [`game.md`](docs/game.md) | Game, Level, Camera, Particle |
| [`ui.md`](docs/ui.md) | Menu, HUD, GameOver |
| [`world.md`](docs/world.md) | Platform, Trap, PowerUp |
| [`levels.md`](docs/levels.md) | Format des fichiers .txt, caractères de mapping |
| [`api-reference.md`](docs/api-reference.md) | Référence complète de toutes les classes et méthodes |

---

## Licence

Ce projet est open source sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.