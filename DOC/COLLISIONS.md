# Gestion des collisions

## Types de collisions

### 1. Collisions horizontales (Hero/Enemy)
- Détection : intersection entre hitbox et plateforme
- Résolution : ajuster x pour sortir de la plateforme
- Reset de la vitesse horizontale

### 2. Collisions verticales
- **Sol** : vy > 0, y ajusté, vy = 0, onGround = true
- **Plafond** : vy < 0, y ajusté, vy = 0
- **Coyote time** : active 8 frames après le sol

### 3. Jump buffer
- 8 frames pour capter le saut
- Active si coyoteTimer > 0 ET jumpBufferTimer > 0

## Optimisations
- Séparation horizontale/verticale pour éviter les bugs de clip
- Coyote time pour faciliter les sauts
- Jump buffer pour le confort du joueur

## Implémentation

### Hero
```java
horizontalCollisions()
verticalCollisions()
```

### Enemy
```java
update() // collisions intégrées
```

### Résolution
- Déplacement avant collision
- Détection d'intersection
- Ajustement de position
- Reset de vitesse
