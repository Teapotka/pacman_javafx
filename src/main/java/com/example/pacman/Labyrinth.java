package com.example.pacman;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Labyrinth {

    // Konstanty
    private static final int WALL = 1;
    private static final int DOT = 0;
    private static final int EMPTY = 2;
    private static final int EXIT = -1;
    private static final int GHOST = 3;
    private static final int TILE_SIZE = 50;
    private static final Color COLOR_WALL = Color.BLACK;
    private static final Color COLOR_DOT = Color.GREEN;
    private static final Color COLOR_EXIT = Color.RED;

    // Premenne
    private final int[][] labyrinth;
    private final GraphicsContext gc;
    public boolean reseting;

    public Labyrinth(GraphicsContext gc) {
        this.gc = gc;
        this.labyrinth = initializeLabyrinth();
        reseting = false;
        draw();
    }

    private int[][] initializeLabyrinth(){
        return new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
                {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
    }

    private void draw() {
        for (int i = 0; i < labyrinth.length; i++) {
            for (int j = 0; j < labyrinth[i].length; j++) {
               drawTile(i, j);
            }
        }
    }

    private void drawTile(int row, int col){
        int tile = labyrinth[row][col];
        int x = col * TILE_SIZE;
        int y = row * TILE_SIZE;

        if (tile == WALL) {
            gc.setFill(COLOR_WALL);
            if (row < 2)
                gc.fillRect(x, y, TILE_SIZE, TILE_SIZE + 10);
            else
                gc.fillRoundRect(x, y, TILE_SIZE, TILE_SIZE, 10, 10);
        }
        else if (tile == DOT) {
            gc.setFill(COLOR_DOT);
            gc.fillOval(x + 23, y + 23, 4, 4);
        }
    }

    public int getIndex(int x, int y) {
        return labyrinth[y][x];
    }

    public void setEmpty(int x, int y) {
        int tile = labyrinth[y][x];

        if(tile == DOT || tile == EMPTY) {
            labyrinth[y][x] = 2;
            gc.clearRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            if (noDotsLeft() && !reseting) {
                reseting = true;
                drawExits();
            }
        }
    }

    public void setGhost(int x, int y) {
        int tile = labyrinth[y][x];
        if(tile == DOT || tile == EMPTY){
            labyrinth[y][x] += GHOST;
        }
    }
    public void setPrevious(int x, int y) {
        int tile = labyrinth[y][x];
        if(tile == (DOT+GHOST) || tile == (EMPTY+GHOST)) {
            labyrinth[y][x] -= 3;
        }
    }
    public boolean noDotsLeft() {
        for (int[] row : labyrinth) {
            for (int cell : row) {
                if (cell == DOT || cell == (DOT+GHOST)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void drawExits(){
        labyrinth[7][14] = EXIT;
        labyrinth[7][0] = EXIT;
        gc.setStroke(COLOR_EXIT);
        gc.setLineWidth(2.0);

        drawExitArrow((14 * TILE_SIZE) + 15, (7 * TILE_SIZE)+25, 2);
        drawExitArrow(15, (7 * TILE_SIZE) + 25, 0);
    }
    private void drawExitArrow(int startX, int startY, int direction){
        gc.strokeLine(startX, startY, startX + 20, startY);
        gc.strokeLine(startX + 10, startY - 10, startX + (10 * direction), startY);
        gc.strokeLine(startX + 10, startY + 10, startX + (10 * direction), startY);
    }
}
