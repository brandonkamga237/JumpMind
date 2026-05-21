# Design du jeu JumpMind

## Principes de conception

### 1. Séparation des responsabilités
- **Entity**: Base commune pour tous les objets
- **Hero/Enemy/Portal**: Entités spécifiques avec leur logique
- **Platform**: Éléments du monde statiques
- **Game**: Orchestrateur principal
- **Camera**: Suivi de la caméra
- **InputHandler**: Gestion des entrées

### 2. Physique
- Gravité constante appliquée à chaque frame
- Accélération et friction pour le mouvement horizontal
- Coyote time (8 frames) pour faciliter les sauts
- Jump buffer (8 frames) pour capter les sauts intentionnels
- Collision AABB avec résolution correcte

### 3. Collisions
- Détection par intersection de rectangles
- Résolution séparée horizontale et verticale
- Coyote time lors de la collision avec le sol
- Squash/stretch pour le feedback visuel

### 4. Animation
- State-based rendering avec couleurs par état
- Squash/stretch pour le mouvement
- Pulsation pour les portails
- Smooth interpolation des échelles

### 5. Game Feel
- Coyote time : permet de sauter 8 frames après quitter une plateforme
- Jump buffer : enregistre l'intention de saut pour 8 frames
- Squash/stretch : 1.15/0.85 lors du contact sol
- Squash/stretch inverse lors du saut

## Architecture des classes

```
Entity (abstract)
├── Hero
├── Enemy
└── Portal

World
├── Platform
└── Trap/PowerUp

Engine
├── InputHandler
├── GameLoop
└── GamePanel

Game
├── Game
├── Camera
└── Level

UI
├── Menu
├── HUD
└── GameOver
```

## Game Loop
1. Input → 2. Physics → 3. Collisions → 4. State Update → 5. Render

## Gestion des états
Le joueur passe entre 5 états :
- IDLE: vx ≈ 0, onGround = true
- RUN: |vx| > 0.5, onGround = true
- JUMP: onGround = false, vy < 0
- FALL: onGround = false, vy > 0
- DASH: |vx| > MAX_SPEED (non implémenté)
