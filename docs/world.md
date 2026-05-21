# Package `world` — Objets du monde

Le package `world` contient les éléments statiques et interactifs du décor : plateformes, pièges et power-ups.

---

## Platform.java

Les plateformes sont les surfaces sur lesquelles le joueur peut marcher et atterrir.

### Propriétés

| Champ | Type | Description |
|-------|------|-------------|
| `x`, `y` | `int` | Position dans le monde |
| `w`, `h` | `int` | Largeur et hauteur |

### Constructeur

```java
public Platform(int x, int y, int w, int h)
```

Les plateformes créées par le parser de niveau font toujours 32×32 pixels (`TILE_SIZE`).

### Méthodes

| Méthode | Retour | Description |
|---------|--------|-------------|
| `getBounds()` | `Rectangle` | Boîte de collision pour la physique |
| `getX()` / `getY()` | `int` | Accesseurs de position |
| `getW()` / `getH()` | `int` | Accesseurs de dimensions |

### Rendu

Les plateformes ont un style visuel moderne en 4 couches :

| Couche | Couleur | Position | Description |
|--------|---------|---------|-------------|
| Base | `(45, 48, 58)` | Pleine taille | Fond sombre |
| Bord néon | `(120, 200, 255)` | Haut, 4px | Ligne bleue brillante |
| Surbrillance | `(200, 240, 255, 180)` | Haut, 2px | Reflet lumineux |
| Ombre | `(20, 22, 30)` | Bas, 5px | Ombre profonde |

---

## Trap.java

Les pièges sont des zones mortelles représentées par des triangles rouges pointant vers le haut.

### Propriétés

| Champ | Type | Description |
|-------|------|-------------|
| `x`, `y` | `int` | Position |
| `w`, `h` | `int` | Dimensions (32×32 par défaut) |

### Méthodes

| Méthode | Retour | Description |
|---------|--------|-------------|
| `getBounds()` | `Rectangle` | Boîte de collision rectangulaire |

> **Note** : La hitbox est un rectangle, pas un triangle. La forme triangulaire est purement visuelle.

### Rendu

Le piège est rendu comme un triangle rouge vif utilisant `Graphics.fillPolygon()` :
- Pointe en haut
- Base en bas
- Taille correspondant à `w × h`

### Comportement

Dans `Game.update()`, si la hitbox du héros intersecte celle d'un piège :
1. **Screen shake** (intensité 6, durée 11)
2. Appel à `die()` → recharge le niveau

---

## PowerUp.java

Les power-ups sont des objets à collectionner (carrés jaunes de 16×16 pixels).

### Propriétés

| Champ | Type | Description |
|-------|------|-------------|
| `x`, `y` | `int` | Position |
| `SIZE` | `static final int` | Taille fixe (16) |
| `collected` | `boolean` | État de collecte |

### Méthodes

| Méthode | Retour | Description |
|---------|--------|-------------|
| `getBounds()` | `Rectangle` | Boîte de collision |
| `isCollected()` | `boolean` | Vrai si déjà collecté |
| `collect()` | `void` | Marque comme collecté |
| `render(Graphics, int)` | `void` | Rendu (caché si collecté) |

### Rendu

Les power-ups sont des carrés jaunes pleins. Ils ne sont pas rendus si `collected` est `true`.

> **État actuel** : La classe `PowerUp` est définie mais n'est **pas utilisée** dans le jeu (pas de caractère dans le parser de niveau, pas d'instanciation dans `Game`). Elle est prête pour une future implémentation.