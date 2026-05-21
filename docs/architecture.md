# Architecture de JumpMind

Ce document décrit l'architecture globale du projet, le flux d'exécution, les relations entre les packages et les principes de conception.

---

## Vue d'ensemble

JumpMind suit une architecture modulaire organisée en 5 packages principaux :

| Package | Rôle | Classes |
|---------|------|---------|
| `engine` | Moteur de jeu (boucle, rendu, entrées) | `GamePanel`, `GameLoop`, `InputHandler` |
| `entities` | Entités du jeu (joueur, ennemis, portail) | `Entity`, `Hero`, `Enemy`, `Portal`, `PlayerState` |
| `game` | Logique de jeu, niveaux, caméra, particules | `Game`, `Level`, `Camera`, `Particle` |
| `world` | Objets du monde (plateformes, pièges, power-ups) | `Platform`, `Trap`, `PowerUp` |
| `ui` | Interface utilisateur (menu, HUD, game over) | `Menu`, `HUD`, `GameOver` |

---

## Flux d'exécution

```
Main.main()
  └── GamePanel.startGame()
        └── GameLoop.start() (nouveau thread)
              └── Boucle infinie à 60 FPS :
                    ├── GamePanel.update()
                    │     ├── Menu.update()          (écran MENU)
                    │     ├── Game.update()           (écran GAME)
                    │     └── GameOver.update()       (écran GAMEOVER)
                    └── GamePanel.repaint()
                          └── paintComponent()
                                ├── Menu.render()      (écran MENU)
                                ├── Game.render()      (écran GAME)
                                └── GameOver.render()  (écran GAMEOVER)
```

### Machine d'états des écrans

Le `GamePanel` gère une machine d'états à 3 écrans via l'enum `Screen` :

```
MENU ──(Entrée pressée)──> GAME ──(mort)──> GAMEOVER ──(Entrée)──> GAME
                              │                                       │
                              └──(victoire)──> écran WIN              │
                              └──(portail + niveaux restants)──> GAME │
```

---

## Diagramme des dépendances entre packages

```
                  ┌──────────────────────────────────────┐
                  │              engine                   │
                  │  GamePanel ◄── GameLoop               │
                  │  InputHandler                         │
                  └──────────┬───────────────────────────┘
                             │ utilise
              ┌──────────────┼──────────────┐
              ▼              ▼              ▼
        ┌──────────┐  ┌──────────┐  ┌──────────┐
        │    ui    │  │   game   │  │ entities │
        │  Menu    │  │  Game    │  │  Hero    │
        │  HUD     │  │  Level   │  │  Enemy   │
        │  GameOver│  │  Camera  │  │  Portal  │
        └──────────┘  │  Particle│  │  Entity  │
                      └────┬─────┘  └────┬─────┘
                           │utilise     │utilise
                           ▼            ▼
                      ┌──────────────────────┐
                      │        world         │
                      │  Platform            │
                      │  Trap                │
                      │  PowerUp             │
                      └──────────────────────┘
```

---

## Principes de conception

### 1. Séparation des responsabilités
Chaque package a une responsabilité unique et bien définie :
- `engine` : infrastructure technique
- `entities` : comportements des entités
- `game` : orchestration du gameplay
- `world` : éléments statiques du décor
- `ui` : affichage des écrans et informations

### 2. Héritage et polymorphisme
La classe abstraite `Entity` définit l'interface commune à toutes les entités :
- `update()` : mise à jour logique
- `render(Graphics2D, int, int)` : rendu graphique
- `getBounds()` : boîte de collision

`Hero`, `Enemy` et `Portal` héritent de `Entity` et spécialisent ces méthodes.

### 3. Machine d'états
Deux systèmes utilisent des machines d'états :
- **Écrans** (`GamePanel`) : 3 états (MENU, GAME, GAMEOVER)
- **Joueur** (`PlayerState`) : 6 états (IDLE, RUN, JUMP, FALL, DASH, WALL_SLIDE)
- **Jeu** (`Game.State`) : 3 états (PLAYING, DEAD, WIN)

### 4. Boucle de jeu fixe
La `GameLoop` maintient un framerate constant de 60 FPS via un système de delta time basé sur `System.nanoTime()`. La mise à jour et le rendu sont synchronisés.

### 5. Chargement de niveaux par fichiers texte
Les niveaux sont définis dans des fichiers `.txt` et parsés caractère par caractère par la classe `Level`. Cela permet de créer et modifier des niveaux sans recompiler le code.

---

## Point d'entrée : Main.java

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        GamePanel panel = new GamePanel();
        panel.startGame();
    });
}
```

Le point d'entrée est minimal : il crée le panneau de jeu et démarre la boucle sur le thread EDT de Swing.