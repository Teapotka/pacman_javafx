package com.example.pacman;

public interface GameLogic {
    int checkTile(int x, int y);
    void setEmpty(int x, int y);
    void setGhost(int x, int y);
    void setPrevious(int x, int y);
    void resetGame(int status);
}
