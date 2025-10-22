package controller;

import game.GameManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import objects.Paddle;

import java.util.HashSet;
import java.util.Set;


public class InputHandler {
    private Set<KeyCode> activeKeys = new HashSet<>();
    private Paddle paddle;
    private GameManager gameManager;
    private boolean pWasPressed = false;

    public InputHandler(Scene scene, Paddle paddle, GameManager gameManager) {
        this.paddle = paddle;
        this.gameManager = gameManager;
        // When push a key, add to Set
        scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
        // When pull a key, remove from Set
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
    }

    public void handleInput() {
        // paddle
        if (activeKeys.contains(KeyCode.LEFT)) {
            paddle.moveLeft();
        } else if (activeKeys.contains(KeyCode.RIGHT)) {
            paddle.moveRight();
        }

        // pause game
        if (activeKeys.contains(KeyCode.P)) {
            if (!pWasPressed) {
                gameManager.togglePause();
                pWasPressed = true;
            }
        } else {
            pWasPressed = false;
        }

        // exit game
        if (activeKeys.contains(KeyCode.ESCAPE)) {
            Platform.exit();
        }
    }
}
