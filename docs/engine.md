# Package `engine` — Moteur de jeu

Le package `engine` contient l'infrastructure technique du jeu : la boucle de jeu, la gestion des entrées clavier et le panneau d'affichage Swing.

---

## GamePanel.java

`GamePanel` est le composant principal de l'interface graphique. Il étend `JPanel` et orchestre le cycle de vie du jeu.

### Champs statiques

```java
public static final int WIDTH = 800;
public static final int HEIGHT = 480;
```

La résolution du jeu est fixe : **800×480 pixels**. La fenêtre n'est pas redimensionnable.

### Screen (enum interne)

```java
private enum Screen { MENU, GAME, GAMEOVER }
```

Machine d'états qui détermine quel écran est affiché et mis à jour.

### Constructeur

```java
public GamePanel()
```

1. Configure la taille (800×480), le fond noir et le focus clavier
2. Crée l'`InputHandler` et l'enregistre comme `KeyListener`
3. Instancie `Menu`, `GameOver` et `Game`
4. Crée la `JFrame` "JumpMind" (non redimensionnable, centrée)
5. Crée la `GameLoop`

### Méthodes principales

| Méthode | Description |
|---------|-------------|
| `startGame()` | Demande le focus et démarre la boucle de jeu |
| `update()` | Délègue la mise à jour à l'écran actif |
| `paintComponent(Graphics)` | Délègue le rendu à l'écran actif |

---

## GameLoop.java

`GameLoop` implémente `Runnable` et exécute la boucle de jeu dans un thread séparé.

### Boucle à 60 FPS

```java
private static final int FPS = 60;
private static final long FRAME_TIME = 1_000_000_000 / FPS; // ~16.67 ms
```

La boucle utilise `System.nanoTime()` pour mesurer le delta time et n'exécute une frame que lorsque le temps écoulé depuis la dernière frame atteint `FRAME_TIME`.

### Algorithme

```
tant que running :
    delta = maintenant - dernièreFrame
    si delta >= FRAME_TIME :
        panel.update()
        panel.repaint()
        dernièreFrame = maintenant
```

### Méthodes

| Méthode | Description |
|---------|-------------|
| `start()` | Passe `running` à `true` et lance un nouveau thread |
| `stop()` | Passe `running` à `false` (arrêt propre) |
| `run()` | Boucle principale à 60 FPS |

> **Note** : `stop()` n'est jamais appelé dans le code actuel, mais est disponible pour un arrêt propre.

---

## InputHandler.java

`InputHandler` implémente `KeyListener` et maintient un ensemble de touches actuellement pressées.

### Architecture

```java
private final Set<Integer> keys = new HashSet<>();
```

- `keyPressed(KeyEvent)` : ajoute le code de la touche au set
- `keyReleased(KeyEvent)` : retire le code de la touche du set
- `keyTyped(KeyEvent)` : ignoré

### Méthode principale

```java
public boolean isPressed(int keyCode)
```

Retourne `true` si la touche correspondant au `keyCode` est actuellement pressée.

### Utilisation

L'`InputHandler` est passé au constructeur de `Game`, qui le transmet à `Hero`. Le héros interroge ensuite les touches à chaque frame :

```java
boolean left = input.isPressed(KeyEvent.VK_A) || input.isPressed(KeyEvent.VK_LEFT);
boolean right = input.isPressed(KeyEvent.VK_D) || input.isPressed(KeyEvent.VK_RIGHT);
boolean jump = input.isPressed(KeyEvent.VK_SPACE) || input.isPressed(KeyEvent.VK_W) || input.isPressed(KeyEvent.VK_UP);
boolean dash = input.isPressed(KeyEvent.VK_SHIFT);
```

---

## Diagramme de séquence simplifié

```
GameLoop.run()
    │
    ├── GamePanel.update()
    │     └── Game.update()
    │           ├── Hero.update(platforms)
    │           │     ├── handleInput()
    │           │     │     └── input.isPressed() × N
    │           │     ├── applyPhysics()
    │           │     ├── horizontalCollisions()
    │           │     ├── verticalCollisions()
    │           │     ├── checkGrounded()
    │           │     └── updateShape()
    │           ├── Enemy.update() × N
    │           ├── Camera.update()
    │           └── Particles.update() × N
    │
    └── GamePanel.repaint()
          └── paintComponent()
                └── Game.render()
                      ├── Platform.render() × N
                      ├── Trap.render() × N
                      ├── Enemy.render() × N
                      ├── Portal.render()
                      ├── Hero.render()
                      ├── Particle.render() × N
                      └── HUD.render()