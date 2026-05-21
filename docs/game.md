# Package `game` — Logique de jeu

Le package `game` orchestre le gameplay : gestion des niveaux, caméra, particules et état global du jeu.

---

## Game.java

`Game` est l'orchestrateur central du gameplay. Il gère le héros, le niveau, la caméra, les particules et les transitions de jeu.

### États du jeu

```java
public enum State { PLAYING, DEAD, WIN }
```

| État | Description |
|------|-------------|
| `PLAYING` | Jeu actif, mises à jour normales |
| `DEAD` | Intermédiaire (non utilisé directement, le jeu recharge le niveau) |
| `WIN` | Tous les niveaux complétés, écran de victoire |

### Champs importants

| Champ | Description |
|-------|-------------|
| `currentLevel` | Niveau actuel (1 à 5) |
| `MAX_LEVELS` | Nombre total de niveaux (5) |
| `deaths` | Compteur de morts |
| `cameraFollow` | Caméra suit le joueur (`true` par défaut) |
| `testMode` | Mode test sans mort ni collisions horizontales (`false`) |
| `particles` | Liste de particules visuelles |
| `hitstop` | Compteur de gel d'image (hitstop) |

### Cycle de mise à jour

```java
public void update() {
    if (state != PLAYING) return;
    if (hitstop > 0) { hitstop--; return; }  // Gel temporaire

    hero.update(platforms);              // Mise à jour du joueur
    enemies.update(platforms);           // Mise à jour des ennemis
    camera.update(heroX, heroY);        // Suivi caméra
    
    // --- Scénarios de mort ---
    collisions avec traps → die()
    collisions avec enemies → die()
    chute dans le vide → die()
    
    // --- Victoire ---
    collision avec portail → nextLevel()
    
    // --- Effets visuels ---
    particules.update()
    dash trail
    atterrissage brutal → shake + particules + hitstop
}
```

### Effets visuels (juice)

| Effet | Déclencheur | Détail |
|-------|-------------|--------|
| **Landing dust** | `vy > 8` (chute rapide) | 12 particules grises aléatoires |
| **Dash trail** | État `DASH` | Particules oranges générées à 65% par frame |
| **Screen shake** | Mort, chute | Intensité 4.5–7, durée 9–11 frames |
| **Hitstop** | Chute violente | Gel de 3 frames |

### Gestion de la mort

```java
private void die() {
    deaths++;
    hud.setDeaths(deaths);
    loadLevel(currentLevel);    // Recharge le niveau actuel
}
```

Le joueur réapparaît au point de départ du niveau. Les ennemis et pièges sont réinitialisés.

### Gestion des niveaux

```java
private void nextLevel() {
    if (currentLevel < MAX_LEVELS) {
        currentLevel++;
        loadLevel(currentLevel);
    } else {
        state = State.WIN;
    }
}
```

Quand le joueur atteint le portail du dernier niveau, l'état passe à `WIN` et l'écran de victoire s'affiche.

### Mode test

Le mode test (`testMode = true`) désactive les collisions horizontales et les morts. Un bandeau jaune "TEST MODE" s'affiche en bas de l'écran. Utile pour tester les mouvements librement.

---

## Level.java

`Level` charge et stocke les données d'un niveau à partir d'un fichier texte.

### Taille des tuiles

```java
public static final int TILE_SIZE = 32;
```

Chaque caractère du fichier de niveau correspond à une tuile de 32×32 pixels.

### Parsing du fichier

```java
public void load(String path) {
    // Lit le fichier ligne par ligne
    // Pour chaque caractère :
    //   '#' → Platform (32×32)
    //   '^' → Trap (32×32)
    //   'E' → Enemy (30×30)
    //   'X' → Portal (42×60)
    //   'H' → Point de départ du héros
    //   '.' → Vide
    //   ' ' → Vide
}
```

### Accesseurs

| Méthode | Retour |
|---------|--------|
| `getPlatforms()` | `List<Platform>` |
| `getTraps()` | `List<Trap>` |
| `getEnemies()` | `List<Enemy>` |
| `getPortal()` | `Portal` (ou `null`) |
| `getHeroStartX()` | Position X de départ |
| `getHeroStartY()` | Position Y de départ |

---

## Camera.java

La caméra suit le joueur avec un lissage (lerp) et gère le screen shake.

### Suivi

```java
private static final float LERP = 0.12f;

public void update(float heroX, float heroY) {
    float targetX = heroX - WIDTH / 2f;
    x += (targetX - x) * LERP;    // Suivi horizontal lissé
    float targetY = heroY - HEIGHT / 2f;
    y += (targetY - y) * LERP;    // Suivi vertical lissé
    // Clamp à 0 (pas de valeurs négatives)
}
```

Le lerp de 0.12 crée un suivi fluide avec un léger retard.

### Screen shake

```java
public void shake(float intensity, int duration)

// Pendant le shake :
shakeX = random(-0.5..0.5) * intensity * progress
shakeY = random(-0.5..0.5) * intensity * progress * 0.6
```

Le shake décroît progressivement (`progress` tend vers 0) et s'arrête quand `shakeTime` atteint 0. L'axe Y est atténué à 60% pour un shake plus horizontal.

### Conversion monde → écran

```java
public int toScreenX(float worldX) { return (int)(worldX - x); }
public int toScreenY(float worldY) { return (int)(worldY - y); }
```

---

## Particle.java

Système de particules simple pour les effets visuels.

### Propriétés

| Champ | Description |
|-------|-------------|
| `x`, `y` | Position |
| `vx`, `vy` | Vélocité |
| `life` / `maxLife` | Durée de vie restante / initiale |
| `color` | Couleur de base |
| `size` | Taille en pixels |

### Physique des particules

```java
public void update() {
    x += vx;
    y += vy;
    life -= 1;
    vy += 0.08f;    // Micro-gravité
    vx *= 0.98f;    // Friction horizontale
}
```

### Rendu

L'alpha décroît linéairement avec la durée de vie : `alpha = (life / maxLife) * 200`. La particule est rendue comme un ovale plein.

### Utilisation dans Game

```java
// Landing dust
for (int i = 0; i < 12; i++) {
    particles.add(new Particle(x, y, random_vx, random_vy, 18-26, GRAY, 3.5-5));
}

// Dash trail
particles.add(new Particle(x, y, small_random_vx, small_random_vy, 9, ORANGE, 4.5));
```

Les particules sont nettoyées automatiquement quand `life <= 0`.