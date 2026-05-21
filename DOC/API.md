# API du jeu JumpMind

## Classe Entity
Classe abstraite de base pour toutes les entités du jeu.

### Méthodes
- `getBounds()`: Retourne le rectangle de collision
- `update()`: Mise à jour logique (à implémenter)
- `render(Graphics2D g, int camX)`: Rendu visuel

## Classe Hero
Représente le joueur.

### Attributs
- `ACCELERATION`, `MAX_SPEED`, `FRICTION`, `AIR_FRICTION`: Constantes de physique
- `GRAVITY`, `MAX_FALL_SPEED`, `JUMP_FORCE`: Constantes de mouvement
- `coyoteTimer`, `jumpBufferTimer`: Mécaniques de jeu feel
- `state`: État actuel du joueur
- `scaleX`, `scaleY`: Squash/stretch animation

### Méthodes
- `update(List<Platform> platforms)`: Mise à jour principale
- `handleInput()`: Gestion des entrées clavier
- `applyPhysics()`: Application de la physique
- `horizontalCollisions()`: Gestion des collisions horizontales
- `verticalCollisions()`: Gestion des collisions verticales
- `updateState()`: Mise à jour de l'état du joueur
- `animate()`: Animation squash/stretch
- `render()`: Rendu avec couleurs selon l'état

## Classe Enemy
Représente un ennemi.

### Attributs
- `SPEED`: Vitesse de déplacement
- `GRAVITY`: Gravité appliquée
- `moveRight`: Direction de déplacement

### Méthodes
- `update(List<Platform> platforms)`: Mise à jour avec IA de patrouille
- `render()`: Rendu en orange

## Classe Portal
Représente un portail.

### Attributs
- `timer`: Compteur d'animation

### Méthodes
- `update()`: Mise à jour de l'animation pulsante
- `render()`: Rendu avec effet de pulsation

## Enum PlayerState
États possibles du joueur :
- `IDLE`: Stationnaire
- `RUN`: En mouvement
- `JUMP`: En saut
- `FALL`: En chute
- `DASH`: En dash
