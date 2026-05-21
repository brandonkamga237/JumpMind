# JumpMind - Plateforme de jeu Java

## Aperçu
JumpMind est un jeu de plateforme 2D développé en Java avec Swing. Le joueur contrôle un héros qui doit traverser des niveaux, éviter les ennemis et atteindre les portails.

## Commandes
- **Déplacer**: A/D ou flèches gauche/droite
- **Sauter**: Espace, W ou flèche haut
- **But**: Atteindre le portail sans toucher les ennemis

## Structure du projet
```
├── entities/          # Entités du jeu (Héros, Ennemis, Portails)
├── world/             # Éléments du monde (Plateformes, Pièges)
├── engine/            # Moteur du jeu (Input, GameLoop, GamePanel)
├── game/              # Logique de jeu (Game, Camera, Level)
├── ui/                # Interface utilisateur (Menu, HUD, GameOver)
└── levels/            # Niveaux du jeu
```

## Comment compiler et exécuter
```bash
find . -name "*.java" > sources.txt
javac @sources.txt
java Main
```

## Architecture
- **Entity**: Classe abstraite pour toutes les entités du jeu (vx/vy, hitbox Rectangle2D.Float)
- **Hero**: Physique avancée (accélération, friction air/sol, gravité), Coyote Time (8 frames), Jump Buffer (8 frames), Squash & Stretch, machine à états (IDLE/RUN/JUMP/FALL/DASH)
- **Enemy**: IA de patrouille avec rebonds intelligents sur murs et sol
- **Portal**: Animation pulsante fluide (sinus + alpha variable)
- **PlayerState**: Enum des états du joueur
- **Platform / Trap**: Éléments du monde statiques
- **Camera**: Suivi fluide avec lerp
- **Level**: Chargement depuis fichiers texte (.txt)
- **Game**: Gestion des niveaux, collisions mortelles, victoire

## Fonctionnalités Game Feel intégrées
- Coyote Time : possibilité de sauter jusqu'à 8 frames après avoir quitté une plateforme
- Jump Buffer : enregistrement de l'intention de saut pendant 8 frames
- Squash & Stretch : déformation visuelle réaliste lors des impacts (sol/saut)
- Accélération/friction différenciée sol/air pour un contrôle précis
- États visuels colorés selon l'action du joueur

Voir le dossier `DOC/` pour la documentation complète (API, collisions, animations, design).
