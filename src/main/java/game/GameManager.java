package game;

import controller.CollisionHandler;
import controller.InputHandler;
import controller.LevelManager;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextInputDialog;
import managers.HighScoreManager;
import objects.*;
import util.Constants;
import view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameManager {
    private AnimationTimer gameLoop;
    private boolean isRunning;
    private GameView gameView;
    private Paddle paddle;
    private Ball ball;
    private InputHandler inputHandler;
    private LevelManager levelManager;
    private Main mainApp;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private CollisionHandler collisionHandler;
    private HighScoreManager highScoreManager;
    private int score;
    private int lives;
    private int currentLevel;
    private boolean isPaused = false;

    public GameManager(GameView gameView, Main mainApp) {
        this.gameView = gameView;
        this.mainApp = mainApp;
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.levelManager = new LevelManager();
        this.collisionHandler = new CollisionHandler(this);
        this.highScoreManager = new HighScoreManager();
    }

    public void start() {
        this.currentLevel = 1;
        this.score = 0;
        this.lives = Constants.INITIAL_LIVES;
        this.isRunning = true;
        setupLevel();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }

    private void setupLevel() {
        // clear (not contain Canvas)
        gameView.getGamePane().getChildren().removeIf(node -> !(node instanceof Canvas));
        bricks.clear();
        powerUps.clear();

        gameView.getRenderer().hideMessage();

        double paddleX = (Constants.SCREEN_WIDTH - Constants.PADDLE_WIDTH) / 2;
        paddle = new Paddle(paddleX, Constants.PADDLE_START_Y);
        // set the default size for paddle
        paddle.resetSize();

        ball = new Ball(paddleX + (Constants.PADDLE_WIDTH / 2) - Constants. BALL_RADIUS,
                Constants.PADDLE_START_Y - Constants.BALL_RADIUS * 2 - 30);
        bricks.addAll(levelManager.loadLevel(currentLevel));


        gameView.getGamePane().getChildren().addAll(paddle.getView(), ball.getView());
        for (Brick brick : bricks) {
            gameView.getGamePane().getChildren().add(brick.getView());
        }

        inputHandler = new InputHandler(gameView.getScene(), paddle, this);

        gameView.getRenderer().updateScore(score);
        gameView.getRenderer().updateLives(lives);
        gameView.getRenderer().hideMessage();
    }

    private void update() {
        if (!isRunning || isPaused) return;

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

            PowerUp powerUp;
            double rand = Math.random();

            // 50% ra Expand, 50% ra FastBall
            if (rand < 0.5) {
                powerUp = new ExpandPaddlePowerUp(x, y);
            } else {
                powerUp = new FastBallPowerUp(x, y);
            }

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

        // Dùng Platform.runLater để đảm bảo dialog hiển thị đúng lúc
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog("Player");
            dialog.setTitle("Game Over");
            dialog.setHeaderText("Your score: " + score);
            dialog.setContentText("Enter your name:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                new HighScoreManager().addScore(name, score);
                returnToMenu(500);
            });
            // Nếu người dùng đóng dialog, quay về menu
            if (result.isEmpty()) {
                returnToMenu(500);
            }
        });
    }

    private void winGame() {
        isRunning = false;
        gameLoop.stop();

        if (currentLevel > Constants.MAX_LEVEL) {
            gameView.getRenderer().showMessage("You Win!");
            // delay 3s to return menu
            returnToMenu(3000);
        } else {
            gameView.getRenderer().showMessage("LEVEL " + (currentLevel - 1) + " CLEARD!");
            // delay 2s to go to next level
            returnToNextLeve(2000);
        }
    }

    // Make a delay when go to menu
    private void returnToMenu(long delayMillis) {
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

    // Make a delay when go to a new level
    private void returnToNextLeve(long delayMillis) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(delayMillis);
                return null;
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            isRunning = true;
            setupLevel();
            gameLoop.start();
        }));
        new Thread(task).start();
    }

    public void increaseScore(int amount) {
        score += amount;
        gameView.getRenderer().updateScore(score);
    }

    public void togglePause() {
        if (!isRunning) return; // Không pause khi game chưa chạy
        isPaused = !isPaused;
        if (isPaused) {
            gameLoop.stop();
            gameView.getRenderer().showMessage("PAUSED");
        } else {
            gameView.getRenderer().hideMessage();
            gameLoop.start();
        }
    }

}

