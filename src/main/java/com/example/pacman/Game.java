package com.example.pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements GameLogic {

    //Konstanty
    private static final int CANVAS_WIDTH = 750;
    private static final int CANVAS_HEIGHT = 750;
    private static final double GAME_SPEED = 0.2;
    private static final String FONT_ULR = "/font.ttf";
    private static final int FONT_SIZE = 20;
    private static final String MUSIC_GAME_URL = "/wakawaka.mp3";
    private static final String MUSIC_RESULT_URL = "/result.mp3";
    private final Font FONT = Font.loadFont(getClass().getResourceAsStream(FONT_ULR), FONT_SIZE);
    private final Group root;
    private final Scene scene;
    private final GraphicsContext gc;

    // Premenne
    private Labyrinth labyrinth;
    private Hero hero;
    private List<Ghost> ghosts;
    private Text levelText;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    private int level = 1;
    private Timeline gameLoop;

    // Flags
    private boolean started = false;
    private boolean moveGhosts = true;

    public Game(Group root, Scene scene) {
        super(CANVAS_WIDTH, CANVAS_HEIGHT);

        this.root = root;
        this.scene = scene;
        gc = getGraphicsContext2D();

        root.getChildren().add(this);

        initializeGameComponents();
        setupGameLoop();
        startGame();
    }

    private void initializeGameComponents() {
        this.labyrinth = new Labyrinth(gc);
        this.hero = new Hero(this);
        this.ghosts = createGhosts();
        this.levelText = createLevelText();
        this.mediaView = createMediaView();

        root.getChildren().add(hero);
        root.getChildren().add(levelText);
        for (Ghost ghost : ghosts) {
            root.getChildren().add(ghost);
        }

        scene.setOnKeyPressed(this::handleKeyPressed);
    }

    private List<Ghost> createGhosts() {
        List<Ghost> ghostList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ghostList.add(new Ghost(this, i));
        }
        return ghostList;
    }

    private Text createLevelText() {
        Text text = new Text("Level " + level);
        text.setFont(FONT);
        text.setFill(Color.WHITE);
        text.setLayoutX(50);
        text.setLayoutY(50);
        return text;
    }

    private MediaView createMediaView() {
        mediaPlayer = new MediaPlayer(
                new Media(getClass().getResource(MUSIC_GAME_URL).toExternalForm())
        );
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        if (mediaView == null) {
            MediaView mediaView = new MediaView(mediaPlayer);
            root.getChildren().add(mediaView);
            return mediaView;
        } else {
            return mediaView;
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE && !started) {
            started = true;
            mediaPlayer.play();
        }
        if (started)
            switch (event.getCode()) {
                case RIGHT, D -> hero.setDirection(0);
                case DOWN, S -> hero.setDirection(1);
                case LEFT, A -> hero.setDirection(2);
                case UP, W -> hero.setDirection(3);
            }
    }

    private void setupGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.seconds(GAME_SPEED), actionEvent -> {
            if (started) {
                hero.move();
                if (moveGhosts) {
                    for (Ghost ghost : ghosts) {
                        ghost.move();
                    }
                    moveGhosts = false;
                } else
                    moveGhosts = true;
                for (Ghost ghost : ghosts) {
                    hero.checkCollision(ghost.getPositionX(), ghost.getPositionY());
                }
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    private void startGame() {
        gameLoop.play();
    }

    @Override
    public int checkTile(int x, int y) {
        return labyrinth.getIndex(x, y);
    }

    @Override
    public void setEmpty(int x, int y) {
        labyrinth.setEmpty(x, y);
    }

    @Override
    public void setGhost(int x, int y) {
        labyrinth.setGhost(x, y);
    }

    @Override
    public void setPrevious(int x, int y) {
        labyrinth.setPrevious(x, y);
    }

    @Override
    public void resetGame(int status) {
        mediaPlayer.stop();
        gameLoop.stop();
        deinitializeGameComponents().setOnFinished(actionEvent -> {
            if (status == 2) {
                nextLevel();
            } else {
                endGame(status == 0);
            }
        });
    }

    private void nextLevel() {
        level++;
        if (level < 4) {
            resetScene();
        } else {
            endGame(false);
        }
    }

    private void endGame(boolean gameOver) {
        gc.clearRect(0, 0, 750, 750);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 750, 750);

        Text resultText = new Text(gameOver ? "Game   Over!\nPress   R   to   Restart"
                : "You   Won!\nPress   R   to   Restart");
        resultText.setFont(FONT);
        resultText.setFill(Color.WHITE);
        resultText.setLayoutX(230);
        resultText.setLayoutY(250);

        mediaPlayer = new MediaPlayer(new Media(getClass().getResource(MUSIC_RESULT_URL).toExternalForm()));
        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();

        root.getChildren().add(resultText);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.R) {
                scene.setOnKeyPressed(null);
                gc.clearRect(0, 0, 750, 750);
                root.getChildren().remove(resultText);
                level = 0;
                nextLevel();
            }
        });
    }

    private void resetScene() {
        gc.clearRect(0, 0, 750, 750);
        initializeGameComponents();
        setupGameLoop();
        startGame();
    }

    private Timeline deinitializeGameComponents() {
        this.root.getChildren().remove(hero);
        this.root.getChildren().remove(levelText);
        for (Ghost ghost : ghosts) {
            root.getChildren().remove(ghost);
        }
        this.gameLoop = null;
        this.started = false;
        this.moveGhosts = true;

        Timeline t = new Timeline(new KeyFrame(Duration.seconds(GAME_SPEED), actionEvent -> {
            this.hero = null;
            this.ghosts.clear();
            this.labyrinth = null;
        }));
        t.play();
        return t;
    }
}