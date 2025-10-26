package controller;

import game.GameManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import objects.Paddle;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.Set;


public class InputHandler {
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final Paddle paddle;
    private final GameManager gameManager;
    private boolean pWasPressed = false;

    public InputHandler(Scene scene, Paddle paddle, GameManager gameManager) {
        this.paddle = paddle;
        this.gameManager = gameManager;

        setupInputListeners(scene);
        scene.getRoot().requestFocus();
    }

    // get key from player and handle
    private void setupInputListeners(Scene scene) {
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
    }

    // hande event-based
    private void onKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        activeKeys.add(code);

        switch (code) {
            case P:
                handleTogglePause();
                break;
            case ESCAPE:
                gameManager.returnToMenu(250);
                break;
            default:
                break;
        }
    }

    private void onKeyReleased(KeyEvent event) {
        KeyCode code = event.getCode();
        activeKeys.remove(code);

        if (code == KeyCode.P) {
            pWasPressed = false;
        }
    }

    private void handleTogglePause() {
        if (!pWasPressed) {
            gameManager.togglePause();
            pWasPressed = true;
        }
    }

    // hande state-based
    public void update() {
        boolean leftPressed = activeKeys.contains(KeyCode.LEFT);
        boolean rightPressed = activeKeys.contains(KeyCode.RIGHT);

        if (activeKeys.contains(KeyCode.LEFT) && !rightPressed) {
            paddle.moveLeft();
        } else if (activeKeys.contains(KeyCode.RIGHT) && !leftPressed) {
            paddle.moveRight();
        }
    }
}
