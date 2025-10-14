package game;

import controller.CollisionHandler;
import controller.InputHandler;
import javafx.scene.Node;
import objects.*;
import util.Constants;
import view.GameView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;

public class GameManager {
    private AnimationTimer gameLoop;
    private boolean isRunning;
    private GameView gameView;
    private Paddle paddle;
    private Ball ball;
    private InputHandler inputHandler;
    private Main mainApp;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private CollisionHandler collisionHandler;
    private int score;
    private int lives;

    public GameManager(GameView gameView, Main mainApp) {
        this.gameView = gameView;
        this.mainApp = mainApp;
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.collisionHandler = new CollisionHandler(this);
    }

    public void start() {
        this.score = 0;
        this.lives = Constants.INITIAL_LIVES;
        this.isRunning = true;
        setupGame();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }

    private void setupGame() {
        // clear (not contain Canvas)
        gameView.getGamePane().getChildren().removeIf(node -> !(node instanceof Canvas));
        bricks.clear();
        powerUps.clear();

        double paddleX = (Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH) / 2;
        paddle = new Paddle(paddleX, Constants.PADDLE_START_Y);
        // set the default size for paddle
        paddle.resetSize();

        ball = new Ball(paddleX + (Constants.PADDLE_WIDTH / 2) - Constants. BALL_RADIUS,
                Constants.PADDLE_START_Y - Constants.BALL_RADIUS * 2 - 30);


        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                double x = Constants.BRICK_START_X + col * (Constants.BRICK_WIDTH + Constants.BRICK_GAP_X);
                double y = Constants.BRICK_START_Y + row * (Constants.BRICK_HEIGHT + Constants.BRICK_GAP_Y);

                Brick brick;

                if (row < 2) {
                    brick = new StrongBrick(x, y);
                } else {
                    brick = new NormalBrick(x, y);
                }
                bricks.add(brick);
            }
        }

        gameView.getGamePane().getChildren().addAll(paddle.getView(), ball.getView());
        bricks.forEach(brick -> gameView.getGamePane().getChildren().add(brick.getView()));

        inputHandler = new InputHandler(gameView.getScene(), paddle);

        gameView.getRenderer().updateScore(score);
        gameView.getRenderer().updateLives(lives);
        gameView.getRenderer().hideMessage();
    }

    private void update() {
        if (!isRunning) return;

        inputHandler.handleInput();

        ball.move();

        for (PowerUp powerUp : powerUps) {
            powerUp.move();
        }

        collisionHandler.checkCollisions(ball, paddle, bricks, powerUps);
        removeUsedObjects();
        checkGameState();
    }

    // spawn powerup from brick which is destroyed
    public void spawnPowerUp(Brick brick) {
        if (Math.random() < Constants.POWER_SPAWN_CHANE) {
            double x = brick.getX() + (brick.getWidth() / 2) - (Constants.POWER_UP_SIZE / 2);
            double y = brick.getY();

            PowerUp powerUp = new ExpandPaddlePowerUp(x, y);
            powerUps.add(powerUp);
            gameView.getGamePane().getChildren().add(powerUp.getView());
        }
    }

    // remove object which not need
    private void removeUsedObjects() {
        removeDestroyedBricks();
        removeUsedPowerUps();
    }

    private void removeDestroyedBricks() {
        List<Brick> destroyed = bricks.stream()
                .filter(Brick::isDestroyed)
                .collect(Collectors.toList());

        if (destroyed.isEmpty()) {
            return;
        }

        List<Node> views = new ArrayList<>();
        for (Brick brick : destroyed) {
            Node view = brick.getView();
            if (view != null) {
                views.add(view);
            }
        }

        // Remove from scence
        gameView.getGamePane().getChildren().removeAll(views);

        // Remove from logic
        bricks.removeAll(destroyed);
    }

    private void removeUsedPowerUps() {
        List<PowerUp> used = powerUps.stream()
                .filter(PowerUp::isCollected)
                .collect(Collectors.toList());

        if (used.isEmpty()) {
            return;
        }

        List<Node> views = new ArrayList<>();
        for (PowerUp powerUp : used) {
            Node view = powerUp.getView();
            if (view != null) {
                views.add(view);
            }
        }

        // Remove from scence
        gameView.getGamePane().getChildren().removeAll(views);

        // Remove from logic
        powerUps.removeAll(used);
    }


    private void checkGameState() {
        if (bricks.isEmpty()) {
            winGame();
        }
    }

    public void loseLife() {
        lives--;
        gameView.getRenderer().updateLives(lives);
        // Set the default size for paddle
        paddle.resetSize();

        if (lives <= 0) {
            gameOver();
        } else {
            ball.reset(paddle.getX(), paddle.getWidth());
        }
    }

    private void gameOver() {
        isRunning = false;
        gameLoop.stop();
        gameView.getRenderer().showMessage("Game Over");
        returnToMenuAfterDelay(3000);
    }

    private void winGame() {
        isRunning = false;
        gameLoop.stop();
        gameView.getRenderer().showMessage("You Win!");
        returnToMenuAfterDelay(3000);
    }

    // Make a delay when go to menu
    private void returnToMenuAfterDelay(long delayMillis) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(delayMillis);
                return null;
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> mainApp.showMenu()));
        new Thread(task).start();
    }

    public void increaseScore(int amount) {
        score += amount;
        gameView.getRenderer().updateScore(score);
    }

}

