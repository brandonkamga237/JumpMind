# Documentation JumpMind

## Versions intégrées
Les fichiers sources suivants ont été mis à jour avec les implémentations **exactes** fournies :

- `entities/Entity.java` (avec vx, vy, hitbox Rectangle2D.Float, updateHitbox)
- `entities/PlayerState.java` (enum complet)
- `entities/Hero.java` (physique complète + coyote time + jump buffer + squash/stretch + machine à états)
- `entities/Enemy.java` (patrouille intelligente)
- `entities/Portal.java` (animation pulsante)

## Corrections de compatibilité ajoutées
- Ajout de getters publics dans `Entity.java` : `getX()`, `getY()`, `getWidth()`, `getHeight()`, `getVx()`, `getVy()`
- Cast `Graphics2D` dans `Game.render()` pour les appels aux méthodes `render(Graphics2D, int)`

## Compilation
Le projet compile sans erreur :
```bash
javac @sources.txt
java Main
```

## Contenu de la documentation
- `API.md` — Description de l'API des entités
- `DESIGN.md` — Architecture et game feel
- `ENTITY_SYSTEM.md` — Détails du système d'entités
- `COLLISIONS.md` — Gestion des collisions (coyote, buffer, etc.)
- `ANIMATIONS.md` — Squash & Stretch + couleurs par état

## Points forts du code intégré
- Physique responsive (accélération/friction air/sol)
- Game feel pro : Coyote Time (8 frames) + Jump Buffer (8 frames)
- Animations dynamiques avec scaleX/scaleY
- Machine à états claire (IDLE/RUN/JUMP/FALL/DASH)
- Collisions séparées horizontal/vertical pour éviter les bugs
- Portail avec effet de pulsation fluide
- Ennemis avec rebond intelligent sur murs et sol

Le jeu est maintenant prêt à être étendu (dash, power-ups, meilleurs niveaux, etc.).
