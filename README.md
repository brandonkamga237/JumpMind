# JumpMind

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Platform](https://img.shields.io/badge/Platform-Desktop-lightgrey)
![License](https://img.shields.io/badge/License-MIT-green)

Jeu de plateforme 2D en Java/Swing. Contrôlez une boule lumineuse à travers 5 niveaux remplis de pièges et d'ennemis pour atteindre le portail de sortie.

## Gameplay

| Élément | Apparence | Effet |
|---------|-----------|-------|
| Portail | Cercle vert pulsant | Passage au niveau suivant |
| Pièges | Triangles rouges | Mort instantanée |
| Ennemis | Carrés orange | Mort instantanée |
| Vide | Chute sous la map | Mort |

**Mécaniques :** Course, saut, dash (Shift), wall slide, wall jump, coyote time, jump buffer.

## Contrôles

| Touche | Action |
|--------|--------|
| `A` `D` / `←` `→` | Déplacement |
| `Espace` `W` `↑` | Saut |
| `Shift` | Dash |
| `Entrée` | Menu / Recommencer |

## Compilation et exécution

```bash
javac Main.java engine/*.java entities/*.java game/*.java world/*.java ui/*.java
java Main
```

## Structure

```
├── Main.java              # Point d'entrée
├── engine/                # GamePanel, GameLoop (60 FPS), InputHandler
├── entities/              # Entity, Hero, Enemy, Portal, PlayerState
├── game/                  # Game, Level, Camera, Particle
├── world/                 # Platform, Trap, PowerUp
├── ui/                    # Menu, HUD, GameOver
├── levels/
│   ├── level1.txt         # Tutoriel (sauts simples)
│   ├── level2.txt         # Introduction pièges
│   ├── level3.txt         # Ennemis + dash
│   ├── level4.txt         # Wall jumps, précision
│   └── level5.txt         # Challenge final
└── docs/                  # Documentation détaillée par package
```

## Créer un niveau

Les niveaux sont des fichiers texte (50 colonnes max, hauteur libre). Chaque caractère = une tuile de 32×32 pixels.

| Caractère | Élément |
|-----------|---------|
| `#` | Plateforme |
| `^` | Piège (tue au contact) |
| `E` | Ennemi |
| `X` | Portail de sortie |
| `H` | Point de départ du joueur |
| `.` | Vide |

**Exemple minimal :**

```
..................................................
.....................................X............
...................................####...........
..................................................
H...###..........................................
##################################################
```

**Règles :** Un seul `H` et un seul `X` par niveau. Toujours placer `H` au-dessus d'une plateforme. Le sol en bas évite les chutes infinies.

## Documentation

Documentation détaillée dans [`docs/`](docs/) : architecture, moteur, entités, gameplay, interface, monde, niveaux et référence API complète.

## Licence

MIT