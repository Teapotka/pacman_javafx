# Pac-Man Game  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/Pac_Man.svg/1200px-Pac_Man.svg.png" width=30></img>
A JavaFX implementation of the classic Pac-Man game. This project includes custom logic for the labyrinth, movement mechanics for Pac-Man and ghosts, collision detection, and winning conditions.

## Features
- Labyrinth Logic: Custom grid-based labyrinth implementation to define paths and barriers.
- Pac-Man Movement: Smooth movement within the labyrinth while avoiding walls.
- Ghost Logic: Ghosts navigate the labyrinth with random movement patterns.
- Collision Detection: Detects interactions between Pac-Man and ghosts.
- Winning Condition: Game ends when all dots in the labyrinth are collected.

## Prerequisites

- Java Development Kit (JDK) 21 or later
- JavaFX libraries (version 21)

## Project structure
```
src/
├── main/
    ├── java/
        ├── com.pacman/
        │   ├── Game.java                // Main Game Engine
        │   ├── Labyrinth.java           // Labyrinth grid logic
        │   ├── GameLogic.java           // Interface for labyrinth operations and game state
        │   ├── Hero.java                // Pac-Man character class
        │   ├── Ghost.java               // Ghost character class
        └── resources/                   // Sprites for Pac-Man and Ghosts, Background music and sound effects, Fonts

```

## Controls
- Arrow Keys/WASD: Move Pac-Man (Up, Down, Left, Right)
- Space Key: Start the Game
- R key: Restart the Game
