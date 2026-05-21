# Système d'animations

## Squash & Stretch
Technique pour donner du poids et de la fluidité aux mouvements.

### Implementation dans Hero
- **Sur le sol** : scaleX = 1.15, scaleY = 0.85
- **Saut** : scaleX = 0.8, scaleY = 1.2
- **Interpolation** : smooth hacia 1.0

### Code
```java
scaleX += (1f - scaleX) * 0.15f;
scaleY += (1f - scaleY) * 0.15f;
```

## State-based Colors
Chaque état a une couleur différente :
- IDLE: CYAN
- RUN: BLEU (0, 180, 255)
- JUMP: BLANC
- FALL: ROSE
- DASH: ORANGE

## Portal Animation
- Pulsation sinusoïdale
- Variation de taille ±6px
- Alpha variable 80-220

## RoundRect Rendering
- RADIUS = 12
- Donne un aspect arrondi
- Appliqué à Hero, Enemy, Portal
