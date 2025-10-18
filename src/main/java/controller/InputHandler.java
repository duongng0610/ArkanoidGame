package controller;

import game.GameManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import objects.Paddle;
import objects.Ball;

import java.util.HashSet;
import java.util.Set;


public class InputHandler {
    private Set<KeyCode> activeKeys = new HashSet<>();
    private Paddle paddle;
    private GameManager gameManager;

    public InputHandler(Scene scene, Paddle paddle, GameManager gameManager) {
        this.paddle = paddle;
        // When push a key, add to Set
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        // When pull a key, remove from Set
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
        this.gameManager = gameManager; // Thêm vào
        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());
            if (event.getCode() == KeyCode.P) {
                gameManager.togglePause();
            }
        });
    }

    public void handleInput() {
        if (activeKeys.contains(KeyCode.LEFT)) {
            paddle.moveLeft();
        } else if (activeKeys.contains(KeyCode.RIGHT)) {
            paddle.moveRight();
        }
    }
}
