# Package `entities` — Entités du jeu

Le package `entities` définit toutes les entités présentes dans le monde de JumpMind : le joueur, les ennemis et les portails. La classe abstraite `Entity` sert de base commune.

---

## Entity.java

Classe abstraite qui définit l'interface commune à toutes les entités du jeu.

### Champs

| Champ | Type | Description |
|-------|------|-------------|
| `x`, `y` | `float` | Position dans le monde |
| `width`, `height` | `float` | Dimensions de l'entité |
| `vx`, `vy` | `float` | Vélocité (pixels par frame) |
| `hitbox` | `Rectangle2D.Float` | Boîte de collision |

### Méthodes

| Méthode | Description |
|---------|-------------|
| `updateHitbox()` | Synchronise la hitbox avec la position |
| `getBounds()` | Retourne la boîte de collision |
| `getX()` / `getY()` | Accesseurs de position |
| `getWidth()` / `getHeight()` | Accesseurs de dimensions |
| `getVx()` / `getVy()` | Accesseurs de vélocité |
| `update()` | **Abstraite** — Logique de mise à jour |
| `render(Graphics2D, int, int)` | **Abstraite** — Rendu graphique |

Les paramètres `camX` et `camY` de `render()` représentent la position de la caméra dans le monde, à soustraire des coordonnées pour obtenir la position écran.

---

## PlayerState.java

Enum qui définit les 6 états possibles du joueur :

| État | Condition |
|------|-----------|
| `IDLE` | Au sol, immobile |
| `RUN` | Au sol, en mouvement |
| `JUMP` | En l'air, vélocité verticale négative (monte) |
| `FALL` | En l'air, vélocité verticale positive (descend) |
| `DASH` | Dash actif |
| `WALL_SLIDE` | Contre un mur, en glissade |

L'état influence la couleur du héros lors du rendu.

---

## Hero.java

La classe la plus complexe du jeu (404 lignes). Le `Hero` est une boule déformable contrôlée par le joueur avec des mécaniques de plateforme avancées.

### Physique

| Constante | Valeur | Rôle |
|-----------|--------|------|
| `ACCELERATION` | 0.55 | Accélération en l'air |
| `GROUND_ACCELERATION` | 0.72 | Accélération au sol |
| `MAX_SPEED` | 5.5 | Vitesse maximale horizontale |
| `FRICTION` | 0.82 | Friction au sol |
| `AIR_FRICTION` | 0.96 | Friction en l'air |
| `GRAVITY` | 0.55 | Gravité standard |
| `MAX_FALL_SPEED` | 12 | Vitesse de chute maximale |
| `JUMP_FORCE` | -12 | Force de saut (négative = vers le haut) |

### Game feel (mécaniques indulgentes)

| Constante | Valeur | Description |
|-----------|--------|-------------|
| `COYOTE_TIME` | 8 frames | Tolérance après avoir quitté le sol |
| `JUMP_BUFFER` | 8 frames | Fenêtre de buffer pour le saut |

Le **coyote time** permet au joueur de sauter quelques frames après avoir quitté le bord d'une plateforme. Le **jump buffer** permet d'enregistrer une pression de saut juste avant d'atterrir.

### Dash

| Constante | Valeur | Description |
|-----------|--------|-------------|
| `DASH_SPEED` | 11.5 | Vitesse pendant le dash |
| `DASH_DURATION` | 12 frames | Durée du dash |
| `DASH_COOLDOWN` | 28 frames | Cooldown entre deux dashs |

Le dash fige la vélocité horizontale à `DASH_SPEED` dans la direction du mouvement et réduit la vélocité verticale de 40%.

### Wall jump / slide

| Constante | Valeur | Description |
|-----------|--------|-------------|
| `WALL_SLIDE_GRAVITY` | 0.28 | Gravité réduite contre un mur |
| `WALL_JUMP_FORCE_X` | 6.5 | Force horizontale du wall jump |
| `WALL_JUMP_FORCE_Y` | -11 | Force verticale du wall jump |

Quand le joueur touche un mur latéral sans être au sol, il glisse avec une gravité réduite. Appuyer sur saut déclenche un wall jump qui le propulse dans la direction opposée.

### Système de forme (squash & stretch)

Le héros se déforme dynamiquement selon son état :

| Condition | squashX | squashY |
|-----------|---------|---------|
| Course | +40% | -25% |
| Saut (montée) | 1.25 | 0.75 |
| Chute | 0.85 | 1.25 |
| Dash | 1.6 | 0.65 |
| Mur | 0.8 | 1.2 |

Les déformations utilisent un **double lissage** : `squash` (cible brute) → `visual` (cible lissée) pour un effet fluide.

### Cycle de mise à jour

```java
public void update(List<Platform> platforms) {
    handleInput();        // 1. Lire les entrées
    applyPhysics();       // 2. Appliquer physique
    x += vx;              // 3. Déplacer horizontalement
    updateHitbox();
    horizontalCollisions(); // 4. Résoudre collisions horizontales
    y += vy;              // 5. Déplacer verticalement
    updateHitbox();
    verticalCollisions();   // 6. Résoudre collisions verticales
    checkGrounded();      // 7. Vérifier contact sol
    updateState();        // 8. Mettre à jour l'état
    updateShape();        // 9. Calculer déformations
    updateHitbox();       // 10. Synchroniser hitbox
}
```

### Rendu

Le héros est rendu comme un ovale coloré avec un reflet lumineux et une ombre portée. La couleur dépend de l'état :

| État | Couleur |
|------|---------|
| IDLE | Bleu cyan `(80, 220, 255)` |
| RUN | Bleu vif `(0, 200, 255)` |
| JUMP | Blanc `(255, 255, 255)` |
| FALL | Rose `(255, 120, 180)` |
| DASH | Orange `(255, 140, 40)` |
| WALL_SLIDE | Violet `(180, 140, 255)` |

---

## Enemy.java

Les ennemis sont des carrés orange de 30×30 pixels. Leur comportement est pour l'instant minimal.

```java
public class Enemy extends Entity {
    public Enemy(float x, float y) { super(x, y, 30, 30); }
    public void update(List<Platform> platforms) { /* vide */ }
    public void update() { /* vide */ }
    public void render(Graphics2D g, int camX, int camY) {
        g.setColor(new Color(255, 120, 40));
        g.fillRoundRect(/* ... */);
    }
}
```

> **État actuel** : Les ennemis sont statiques. La méthode `update(List<Platform>)` est prévue mais vide — elle sera implémentée pour ajouter des patrouilles ou des comportements IA.

---

## Portal.java

Le portail est la condition de victoire d'un niveau. C'est un ovale vert pulsant de 42×60 pixels.

### Animation

```java
float pulse = (float)Math.sin(timer) * 6f;  // oscillation ±6px
int alpha = 150 + (int)(Math.sin(timer) * 70); // transparence 150-220
```

L'animation utilise un sinus pour faire pulser la taille et l'alpha du portail, créant un effet de "respiration".

### Détection de collision

Dans `Game.update()`, la hitbox du héros est testée contre celle du portail. En cas d'intersection, le niveau suivant est chargé.