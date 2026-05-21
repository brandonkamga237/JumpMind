# Référence API

Référence complète de toutes les classes, méthodes publiques et constantes du projet JumpMind.

---

## Package `engine`

### GamePanel

```java
public class GamePanel extends JPanel
```

| Membre | Type | Description |
|--------|------|-------------|
| `WIDTH` | `static final int = 800` | Largeur de l'écran |
| `HEIGHT` | `static final int = 480` | Hauteur de l'écran |
| `GamePanel()` | Constructeur | Initialise la fenêtre, les composants et la boucle |
| `startGame()` | `void` | Lance la boucle de jeu |
| `update()` | `void` | Met à jour l'écran actif (Menu/Game/GameOver) |
| `paintComponent(Graphics)` | `void` | Rendu de l'écran actif |

### GameLoop

```java
public class GameLoop implements Runnable
```

| Membre | Type | Description |
|--------|------|-------------|
| `FPS` | `static final int = 60` | Images par seconde |
| `FRAME_TIME` | `static final long = 16_666_667` | Nanosecondes par frame |
| `GameLoop(GamePanel)` | Constructeur | Lie la boucle au panneau |
| `start()` | `void` | Démarre le thread de jeu |
| `stop()` | `void` | Arrête la boucle |
| `run()` | `void` | Boucle principale |

### InputHandler

```java
public class InputHandler implements KeyListener
```

| Membre | Type | Description |
|--------|------|-------------|
| `isPressed(int)` | `boolean` | Vérifie si une touche est pressée |
| `keyPressed(KeyEvent)` | `void` | Enregistre une pression |
| `keyReleased(KeyEvent)` | `void` | Enregistre un relâchement |
| `keyTyped(KeyEvent)` | `void` | Ignoré |

---

## Package `entities`

### Entity (abstraite)

```java
public abstract class Entity
```

| Membre | Type | Description |
|--------|------|-------------|
| `x`, `y` | `protected float` | Position monde |
| `width`, `height` | `protected float` | Dimensions |
| `vx`, `vy` | `protected float` | Vélocité |
| `hitbox` | `protected Rectangle2D.Float` | Boîte de collision |
| `Entity(float, float, float, float)` | Constructeur | x, y, width, height |
| `updateHitbox()` | `protected void` | Synchronise la hitbox |
| `getBounds()` | `Rectangle2D.Float` | Boîte de collision |
| `getX()` | `float` | Position X |
| `getY()` | `float` | Position Y |
| `getWidth()` | `float` | Largeur |
| `getHeight()` | `float` | Hauteur |
| `getVx()` | `float` | Vélocité X |
| `getVy()` | `float` | Vélocité Y |
| `update()` | `abstract void` | Mise à jour |
| `render(Graphics2D, int, int)` | `abstract void` | Rendu (g, camX, camY) |

### PlayerState (enum)

```java
public enum PlayerState
```

| Valeur | Description |
|--------|-------------|
| `IDLE` | Immobile au sol |
| `RUN` | Course au sol |
| `JUMP` | Saut (montée) |
| `FALL` | Chute (descente) |
| `DASH` | Dash actif |
| `WALL_SLIDE` | Glissade murale |

### Hero

```java
public class Hero extends Entity
```

**Constantes physiques :**

| Constante | Valeur | Description |
|-----------|--------|-------------|
| `ACCELERATION` | 0.55 | Accélération en l'air |
| `GROUND_ACCELERATION` | 0.72 | Accélération au sol |
| `MAX_SPEED` | 5.5 | Vitesse max horizontale |
| `FRICTION` | 0.82 | Friction au sol |
| `AIR_FRICTION` | 0.96 | Friction en l'air |
| `GRAVITY` | 0.55 | Gravité |
| `MAX_FALL_SPEED` | 12.0 | Vitesse de chute max |
| `JUMP_FORCE` | -12.0 | Force de saut |

**Coyote time / Jump buffer :**

| Constante | Valeur |
|-----------|--------|
| `COYOTE_TIME` | 8 |
| `JUMP_BUFFER` | 8 |

**Dash :**

| Constante | Valeur |
|-----------|--------|
| `DASH_SPEED` | 11.5 |
| `DASH_DURATION` | 12 |
| `DASH_COOLDOWN` | 28 |

**Wall :**

| Constante | Valeur |
|-----------|--------|
| `WALL_SLIDE_GRAVITY` | 0.28 |
| `WALL_JUMP_FORCE_X` | 6.5 |
| `WALL_JUMP_FORCE_Y` | -11.0 |

**Champs publics :**

| Champ | Type | Description |
|-------|------|-------------|
| `ignoreHorizontalCollisions` | `boolean` | Mode test |

**Méthodes publiques :**

| Méthode | Signature | Description |
|---------|-----------|-------------|
| `Hero()` | `(float x, float y, InputHandler input)` | Constructeur |
| `update()` | `(List<Platform>)` | Mise à jour avec plateformes |
| `update()` | `()` | Override vide |
| `render()` | `(Graphics2D, int, int)` | Rendu du héros |
| `getState()` | `PlayerState` | État actuel |

### Enemy

```java
public class Enemy extends Entity
```

| Membre | Type | Description |
|--------|------|-------------|
| `Enemy(float, float)` | Constructeur | x, y (taille fixe 30×30) |
| `update(List<Platform>)` | `void` | Mise à jour avec plateformes |
| `update()` | `void` | Override vide |
| `render(Graphics2D, int, int)` | `void` | Rendu |

### Portal

```java
public class Portal extends Entity
```

| Membre | Type | Description |
|--------|------|-------------|
| `Portal(float, float)` | Constructeur | x, y (taille fixe 42×60) |
| `update()` | `void` | Animation de pulsation |
| `render(Graphics2D, int, int)` | `void` | Rendu avec pulsation |

---

## Package `game`

### Game

```java
public class Game
```

**Enum interne :**

```java
public enum State { PLAYING, DEAD, WIN }
```

| Membre | Type | Description |
|--------|------|-------------|
| `Game(InputHandler)` | Constructeur | Initialise le jeu |
| `update()` | `void` | Mise à jour complète |
| `render(Graphics)` | `void` | Rendu complet |
| `getState()` | `State` | État du jeu |

### Level

```java
public class Level
```

| Membre | Type | Description |
|--------|------|-------------|
| `TILE_SIZE` | `static final int = 32` | Taille d'une tuile |
| `load(String)` | `void` | Charge un fichier de niveau |
| `getPlatforms()` | `List<Platform>` | Plateformes du niveau |
| `getTraps()` | `List<Trap>` | Pièges du niveau |
| `getEnemies()` | `List<Enemy>` | Ennemis du niveau |
| `getPortal()` | `Portal` | Portail (ou null) |
| `getHeroStartX()` | `float` | Position X de départ |
| `getHeroStartY()` | `float` | Position Y de départ |

### Camera

```java
public class Camera
```

| Membre | Type | Description |
|--------|------|-------------|
| `update(float, float)` | `void` | Suit heroX, heroY |
| `shake(float, int)` | `void` | Déclenche screen shake |
| `getX()` | `float` | Position X (+ shake) |
| `getY()` | `float` | Position Y (+ shake) |
| `toScreenX(float)` | `int` | Conversion monde → écran X |
| `toScreenY(float)` | `int` | Conversion monde → écran Y |

### Particle

```java
public class Particle
```

| Membre | Type | Description |
|--------|------|-------------|
| `x`, `y` | `float` | Position |
| `vx`, `vy` | `float` | Vélocité |
| `life` | `float` | Vie restante |
| `maxLife` | `float` | Vie initiale |
| `color` | `Color` | Couleur |
| `size` | `float` | Taille |
| `Particle(...)` | Constructeur | (x, y, vx, vy, life, color, size) |
| `update()` | `void` | Physique + décrémente vie |
| `render(Graphics2D, int, int)` | `void` | Rendu avec fondu |

---

## Package `world`

### Platform

```java
public class Platform
```

| Membre | Type | Description |
|--------|------|-------------|
| `Platform(int, int, int, int)` | Constructeur | x, y, w, h |
| `getBounds()` | `Rectangle` | Boîte de collision |
| `getX()` | `int` | Position X |
| `getY()` | `int` | Position Y |
| `getW()` | `int` | Largeur |
| `getH()` | `int` | Hauteur |
| `render(Graphics, int, int)` | `void` | Rendu avec style néon |

### Trap

```java
public class Trap
```

| Membre | Type | Description |
|--------|------|-------------|
| `Trap(int, int, int, int)` | Constructeur | x, y, w, h |
| `getBounds()` | `Rectangle` | Boîte de collision |
| `render(Graphics, int, int)` | `void` | Rendu triangle rouge |

### PowerUp

```java
public class PowerUp
```

| Membre | Type | Description |
|--------|------|-------------|
| `SIZE` | `static final int = 16` | Taille fixe |
| `PowerUp(int, int)` | Constructeur | x, y |
| `getBounds()` | `Rectangle` | Boîte de collision |
| `isCollected()` | `boolean` | État de collecte |
| `collect()` | `void` | Marque comme collecté |
| `render(Graphics, int)` | `void` | Rendu (si non collecté) |

---

## Package `ui`

### Menu

```java
public class Menu
```

| Membre | Type | Description |
|--------|------|-------------|
| `update(InputHandler)` | `void` | Détecte Entrée |
| `isDone()` | `boolean` | Menu terminé ? |
| `render(Graphics, int, int)` | `void` | Rendu du menu |

### GameOver

```java
public class GameOver
```

| Membre | Type | Description |
|--------|------|-------------|
| `update(InputHandler)` | `void` | Détecte Entrée |
| `wantsRestart()` | `boolean` | Redémarrage demandé ? |
| `reset()` | `void` | Réinitialise le flag |
| `render(Graphics, int, int)` | `void` | Rendu overlay |

### HUD

```java
public class HUD
```

| Membre | Type | Description |
|--------|------|-------------|
| `setLevel(int)` | `void` | Définit le niveau affiché |
| `setDeaths(int)` | `void` | Définit le compteur de morts |
| `render(Graphics)` | `void` | Rendu du HUD |

---

## Classe principale

### Main

```java
public class Main
```

| Membre | Type | Description |
|--------|------|-------------|
| `main(String[])` | `static void` | Point d'entrée, lance le jeu sur l'EDT |