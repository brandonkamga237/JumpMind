# Système de niveaux

JumpMind utilise un système de fichiers texte pour définir les niveaux. Chaque niveau est un fichier `.txt` dans le dossier `levels/`.

---

## Format des fichiers

Chaque ligne du fichier correspond à une rangée de tuiles. Chaque caractère correspond à une tuile de 32×32 pixels.

### Mapping des caractères

| Caractère | Élément | Classe | Taille |
|-----------|---------|--------|--------|
| `#` | Plateforme | `Platform` | 32×32 |
| `^` | Piège | `Trap` | 32×32 |
| `E` | Ennemi | `Enemy` | 30×30 |
| `X` | Portail de sortie | `Portal` | 42×60 |
| `H` | Point de départ du héros | — | — |
| `.` | Vide (air) | — | — |
| ` ` | Vide (air) | — | — |

---

## Exemple : level1.txt

```
..................................................
.....................E................x............
.............#####..............#######...........
....####..................###.....................
..............####..........................E......
..###..................E.............####..........
.............#####.............#####..............
......E..................####......................
..#######...........####..........E................
..............E.........................#####......
.....####...............#####......................
.............###..................E.................
..E..................####.....................#####
................#####...........####...............
......####..................E......................
H.....###.........................#####.............
##################################################
```

### Analyse du niveau 1

| Élément | Nombre |
|---------|--------|
| Plateformes (`#`) | ~80 |
| Pièges (`^`) | 0 |
| Ennemis (`E`) | 7 |
| Portail (`X`) | 1? (x minuscule = non reconnu) |
| Héros (`H`) | 1 (en bas à gauche) |

> **Note** : Le caractère `x` (minuscule) n'est pas reconnu par le parser. Seul `X` (majuscule) crée un portail. Dans level1.txt, le portail est en minuscule `x`, ce qui signifie qu'aucun portail n'est créé pour ce niveau dans l'état actuel.

---

## Chargement d'un niveau

```java
// Dans Game.java
private void loadLevel(int num) {
    String path = "levels/level" + num + ".txt";
    level.load(path);
    hero = new Hero(level.getHeroStartX(), level.getHeroStartY(), input);
    // ...
}
```

Le parser (`Level.load()`) lit le fichier ligne par ligne :

```java
try (BufferedReader br = new BufferedReader(new FileReader(path))) {
    String line;
    int row = 0;
    while ((line = br.readLine()) != null) {
        for (int col = 0; col < line.length(); col++) {
            char c = line.charAt(col);
            int px = col * TILE_SIZE;  // 32 pixels par tuile
            int py = row * TILE_SIZE;
            switch (c) {
                case '#': platforms.add(new Platform(px, py, 32, 32)); break;
                case '^': traps.add(new Trap(px, py, 32, 32)); break;
                case 'E': enemies.add(new Enemy(px, py)); break;
                case 'X': portal = new Portal(px, py); break;
                case 'H': heroStartX = px; heroStartY = py; break;
            }
        }
        row++;
    }
}
```

---

## Niveaux disponibles

Le projet contient 3 fichiers de niveau dans le dossier `levels/` :

| Fichier | Taille | Lignes × Colonnes |
|---------|--------|-------------------|
| `level1.txt` | 17 lignes | 17 × 50 |
| `level2.txt` | À vérifier | — |
| `level3.txt` | À vérifier | — |

Le jeu est configuré pour 5 niveaux (`MAX_LEVELS = 5`). Les niveaux 4 et 5 doivent encore être créés.

---

## Créer un nouveau niveau

Pour créer un niveau, il suffit de créer un fichier `levels/levelN.txt` (où N est le numéro) et d'utiliser les caractères de mapping. Exemple minimal :

```
..................................................
..................................................
..................................................
..................................................
.....................X............................
..................................................
........................####......................
.................................####.............
....H.......#################################.....
##################################################
```

Ce niveau contient :
- Un héros (`H`) en bas à gauche
- Un portail (`X`) au centre en hauteur
- Des plateformes (`#`) pour atteindre le portail

### Bonnes pratiques

1. **Toujours inclure un sol** en bas du niveau (ligne de `#`) pour éviter les chutes infinies
2. **Placer `H` au-dessus d'une plateforme** pour que le héros ne tombe pas immédiatement
3. **Inclure exactement un `X`** par niveau (le parser ne gère qu'un seul portail)
4. **Garder une largeur cohérente** (50 colonnes recommandé)
5. **Limiter la hauteur** pour rester dans les limites de la caméra