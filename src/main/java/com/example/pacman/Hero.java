package com.example.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Hero extends ImageView {

    private final GameLogic gameLogic;
    private Timeline animation;
    private int direction;
    private int frame;
    private List<Image> frames;
    private int x, y;
    private boolean alive;

    public Hero(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.direction = 0;
        this.frame = 0;
        this.x = 0;
        this.y = 7;
        this.alive = true;

        initializeHero();
    }

    private void initializeHero() {
        positionate();
        loadFrames();
        animate();
    }

    private void positionate() {
        setLayoutX(x * 50);
        setLayoutY(y * 50);
    }

    private void loadFrames() {
        frames = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Image image = new Image(getClass().getResourceAsStream("/pacman" + i + ".png"), 50, 50, false, false);
            frames.add(image);
        }
    }

    private void animate() {
        animation = new Timeline(new KeyFrame(Duration.seconds(0.2), actionEvent -> {
            setImage(frames.get(frame));
            frame = (frame == 1) ? 0 : 1;
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }


    public void setDirection(int newDirection) {
        int stepX = calculateStepX(newDirection);
        int stepY = calculateStepY(newDirection);
        if (isOnEdge(newDirection)) {
            stepX = 0;
        }

        int nextTile = gameLogic.checkTile(x + stepX, y + stepY);
        if (nextTile != 1) {
            this.direction = newDirection;
        }
    }

    public void move() {
        if (!alive) return;

        int stepX = calculateStepX(direction);
        int stepY = calculateStepY(direction);

        if (isOnEdge(direction)) {
            stepX = 0;
        }

        int nextTile = gameLogic.checkTile(x + stepX, y + stepY);

        if (nextTile == -1) {
            gameLogic.resetGame(2);
            return;
        }
        if (isWalkableTile(nextTile)) {
            updatePosition();
            gameLogic.setEmpty(x, y);
        }
    }

    private void updatePosition() {
        switch (direction) {
            case 0 -> { setRotate(0); x = (x == 14) ? 0 : x + 1; }
            case 1 -> { setRotate(90); y += 1; }
            case 2 -> { setRotate(180); x = (x == 0) ? 14 : x - 1; }
            case 3 -> { setRotate(270); y -= 1; }
        }
        positionate();
    }

    private int calculateStepX(int direction) {
        return (direction == 0) ? 1 : (direction == 2) ? -1 : 0;
    }

    private int calculateStepY(int direction) {
        return (direction == 1) ? 1 : (direction == 3) ? -1 : 0;
    }

    private boolean isOnEdge(int direction) {
        return (direction == 0 && x == 14) || (direction == 2 && x == 0);
    }

    private boolean isWalkableTile(int tile){
        return tile == 0 || tile == 2 || tile == 3 || tile == 5;
    }

    public boolean getAlive() {
        return alive;
    }

    public void checkCollision(int ghostX, int ghostY) {
        if (this.x == ghostX && this.y == ghostY && alive) {
            alive = false;
            animation.stop();
            setRotate(0);
            setImage(frames.get(2));
            gameLogic.resetGame(0);
        }
    }
}

