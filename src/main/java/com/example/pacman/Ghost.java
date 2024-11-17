package com.example.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost extends ImageView {
    private final GameLogic labyrinthLogic;
    private final int type;
    private final Random random;
    private int x, y;
    private int direction;

    public Ghost(GameLogic tileChecker, int type) {
        this.labyrinthLogic = tileChecker;

        this.type = type;
        this.x = 5 + this.type;
        this.y = 9;
        this.random = new Random();
        this.direction = random.nextInt(4);

        initializeImage();
        initializePosition();
    }

    private void initializeImage() {
        Image image = new Image(getClass().getResourceAsStream("/ghost" + this.type + ".png"), 50, 50, false, false);
        setImage(image);
    }

    private void initializePosition() {
        labyrinthLogic.setGhost(x, y);
        positionate();
    }

    private void positionate() {
        setLayoutX(x * 50);
        setLayoutY(y * 50);
    }

    public void move() {
        List<Integer> possibleDirections = getAvailableDirections();
        if (possibleDirections.isEmpty()) return;

        direction = possibleDirections.get(random.nextInt(possibleDirections.size()));

        updatePosition();
    }

    private List<Integer> getAvailableDirections() {
        List<Integer> available = new ArrayList<>();
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int newX = (x + dx[i] + 15) % 15;
            int newY = y + dy[i];
            int tile = labyrinthLogic.checkTile(newX, newY);

            if (tile != 1 && tile != 3 && tile != 5) {
                available.add(i);
            }
        }
        return available;
    }

    private void updatePosition() {
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        labyrinthLogic.setPrevious(x, y);
        x = (x + dx[direction] + 15) % 15;
        y += dy[direction];
        positionate();
        labyrinthLogic.setGhost(x, y);
    }

    public int getPositionX() {
        return x;
    }

    public int getPositionY() {
        return y;
    }
}
