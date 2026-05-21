# Système d'entités

## Entity.java
Classe abstraite de base.
- Coordonnées (x, y)
- Dimensions (width, height)
- Hitbox (Rectangle2D.Float)
- Méthodes abstraites : update(), render()

## Hero.java
Héros contrôlable par le joueur.

### Physique
- Vitesse : 5.5 max, 0.55 d'accélération
- Gravité : 0.55
- Saut : -12
- Friction : 0.82 (sol), 0.96 (air)

### Collisions
- Coyote time : 8 frames après quitter le sol
- Jump buffer : 8 frames de saut
- Squash/stretch : 1.15/0.85

### État
IDLE → RUN → JUMP → FALL

## Enemy.java
Ennemis avec IA de patrouille.

### Comportement
- Déplace horizontalement à 2 speed
- Rebondit sur les murs et le sol
- Collision avec les plateformes

## Portal.java
Portail avec animation pulsante.

### Animation
- Pulse basé sur sin(timer)
- Alpha variable 80-220
- Taille variable ±6px

## PlayerState.java
Énumération des états du joueur.
