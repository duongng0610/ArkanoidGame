package game;

import controller.CollisionHandler;
import controller.InputHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import managers.HighScoreManager;
import managers.LevelManager;
import managers.SoundManager;
import objects.*;
import util.Constants;
import util.ImgLoader;
import view.GameView;

import java.util.ArrayList;
import java.util.List;
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
    private List<Image> backgroundImages;
    private List<PowerUp> powerUps;
    private List<Meteorite> meteorites;
    private CollisionHandler collisionHandler;
    private HighScoreManager highScoreManager;
    private SoundManager soundManager;
    private int score;
    private int lives;
    private int currentLevel;
    private boolean isPaused = false;

    public GameManager(GameView gameView, Main mainApp) {
        this.gameView = gameView;
        this.mainApp = mainApp;
        this.bricks = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.meteorites = new ArrayList<>();
        this.levelManager = new LevelManager();
        this.collisionHandler = new CollisionHandler(this);
        this.highScoreManager = HighScoreManager.getInstance();
        this.soundManager = SoundManager.getInstance();
        this.currentLevel = 1;
        loadBackgrounds();
    }

    public void start() {
        this.currentLevel = 1;
        this.score = 0;
        this.lives = Constants.INITIAL_LIVES;
        this.isRunning = true;

        // Reset to default title
        mainApp.resetMenuTitle();

        setupLevel();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
    }

    // Load Background
    private void loadBackgrounds() {
        backgroundImages = new ArrayList<>();
        backgroundImages.add(ImgLoader.loadImage("/assets/levels/bg1.png"));
        backgroundImages.add(ImgLoader.loadImage("/assets/levels/bg2.png"));
        backgroundImages.add(ImgLoader.loadImage("/assets/levels/bg3.png"));
    }

    private void setupLevel() {
        // clear (not contain Canvas)
        gameView.getGamePane().getChildren().removeIf(node -> !(node instanceof Canvas));
        bricks.clear();
        powerUps.clear();
        meteorites.clear();

        gameView.getRenderer().hideMessage();

        if (!backgroundImages.isEmpty()) {
            // When num of levels is bigger than num of background
            int bgIndex = (currentLevel - 1) % backgroundImages.size();
            Image bg = backgroundImages.get(bgIndex);
            gameView.setBackground(bg);
        }

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

        inputHandler.update();

        ball.move();

        for (PowerUp powerUp : powerUps) {
            powerUp.move();
        }

        for (Meteorite meteorite : meteorites) {
            meteorite.move();
        }

        spawnMeteorite();

        collisionHandler.checkCollisions(ball, paddle, bricks, powerUps, meteorites);

        removeUsedObjects();
        checkGameState();
    }

    // spawn meteorite
    private void spawnMeteorite() {
        if (Math.random() < Constants.METEORITE_SPAWN_CHANCE) {
            double x = Math.random() * (Constants.SCREEN_WIDTH - Constants.METEORITE_WIDTH);
            Meteorite meteorite = new Meteorite(x, -Constants.METEORITE_HEIGHT);

            meteorites.add(meteorite);
            gameView.getGamePane().getChildren().add(meteorite.getView());
        }
    }

    // spawn powerup from brick which is destroyed
    public void spawnPowerUp(Brick brick) {
        if (Math.random() < Constants.POWER_SPAWN_CHANCE) {
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
        removeUsedMeteorites();
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

    private void removeUsedMeteorites() {
        List<Meteorite> used = meteorites.stream()
                .filter(o -> o.isDestroyed() || o.getY() > Constants.SCREEN_HEIGHT)
                .collect(Collectors.toList());

        if (used.isEmpty()) {
            return;
        }

        List<Node> views = new ArrayList<>();
        for (Meteorite meteorite : used) {
            Node view = meteorite.getView();
            if (view != null) {
                views.add(view);
            }
        }

        // Remove from scene
        gameView.getGamePane().getChildren().removeAll(views);

        // Remove from logic
        meteorites.removeAll(used);
    }

    private void checkGameState() {
        if (bricks.isEmpty()) {
            winGame();
        }
    }

    // for meteorite when collision with paddle
    public void loseLifeNoReset() {
        lives--;
        gameView.getRenderer().updateLives(lives);
        // Set the default size for paddle
        paddle.resetSize();

        if (lives <= 0) {
            gameOver();
        }
    }

    // for ball when go out screen
    public void loseLife() {
        loseLifeNoReset();

        if (lives > 0) {
            ball.reset(paddle.getX(), paddle.getWidth());
        }
    }

    private void gameOver() {
        isRunning = false;
        gameLoop.stop();

        // update score
        highScoreManager.updateHighScore(this.score);

        // Delay 1 seconds before showing "Game Over" in the menu
        returnToMenuWithGameOver(1000);
    }

    private void winGame() {
        isRunning = false;
        gameLoop.stop();

        // update score
        highScoreManager.updateHighScore(this.score);

        if (currentLevel >= Constants.MAX_LEVEL) {
            gameView.getRenderer().showMessage("You Win!");
            // delay 3s to return menu
            returnToMenu(3000);
        } else {
            gameView.getRenderer().showMessage("LEVEL " + currentLevel + " CLEARD");
            currentLevel++;
            // delay 2s to go to next level
            returnToNextLeve(2000);
        }
    }

    // Make a delay when go to menu
    public void returnToMenu(long delayMillis) {
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

    // Method to delay and set title "Game Over"
    private void returnToMenuWithGameOver(long delayMillis) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(delayMillis);
                return null;
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            mainApp.setMenuTitle("Game Over");
            mainApp.showMenu();
        }));
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
        // can not pause when is not running
        if (!isRunning) {
            return;
        }

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

