package view;

import game.Main;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import managers.HighScoreManager;
import util.Constants;
import util.ImgLoader;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MenuView {
    private Scene scene;
    private Main mainApp;
    private HighScoreManager highScoreManager;
    private Label titleLabel;  // Field để truy cập và thay đổi title

    public MenuView(Main mainApp) {
        this.mainApp = mainApp;
        this.highScoreManager = HighScoreManager.getInstance();

        // Create the main layout with StackPane
        StackPane root = new StackPane();

        // Load background with scrolling effect
        Pane backgroundContainer = loadBackground("/assets/menu/menu.png");

        // Create VBox for buttons and title
        VBox buttonLayout = new VBox(20);
        buttonLayout.setAlignment(Pos.CENTER);

        // Load custom font from .ttf file
        String fontPath = "/assets/menu/Skyscapers.ttf"; // Replace with your .ttf file path
        Font customFont = null;
        try {
            customFont = Font.loadFont(getClass().getResourceAsStream(fontPath), 50); // Load font with size 50
        } catch (Exception e) {
            System.err.println("Error loading font: " + e.getMessage());
            customFont = Font.font("Arial", FontWeight.BOLD, 50); // Fallback font
        }

        // Create title label for "Arkanoid"
        titleLabel = new Label("Arkanoid");
        titleLabel.setFont(customFont != null ? customFont : Font.font("Arial", FontWeight.BOLD, 50));
        titleLabel.setTextFill(Color.ORANGE); // Custom text color
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0.5, 2, 2);"); // Add shadow for visibility
        titleLabel.setTranslateY(-20); // Move title up by 20 pixels

        // Buttons
        Button startButton = createButtonWithImage("/assets/menu/start.png");
        Button highScoresButton = createButtonWithImage("/assets/menu/score.png");
        Button exitButton = createButtonWithImage("/assets/menu/exit.png");

        startButton.setOnAction(e -> this.mainApp.startGame());
        exitButton.setOnAction(e -> this.mainApp.exitGame());
        highScoresButton.setOnAction(e -> showHighScores());

        // Add title and buttons to buttonLayout
        buttonLayout.getChildren().addAll(titleLabel, startButton, highScoresButton, exitButton);

        // Add background and button layout to root
        root.getChildren().addAll(backgroundContainer, buttonLayout);

        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    private Pane loadBackground(String imagePath) {
        Pane backgroundContainer = new Pane();

        if (imagePath != null) {
            // Create two ImageViews for seamless scrolling
            Image backgroundImage = ImgLoader.loadImage(imagePath);
            ImageView backgroundView1 = new ImageView(backgroundImage);
            backgroundView1.setFitWidth(Constants.SCREEN_WIDTH);
            backgroundView1.setFitHeight(Constants.SCREEN_HEIGHT);
            backgroundView1.setPreserveRatio(false); // Stretch to fit screen

            ImageView backgroundView2 = new ImageView(backgroundImage);
            backgroundView2.setFitWidth(Constants.SCREEN_WIDTH);
            backgroundView2.setFitHeight(Constants.SCREEN_HEIGHT);
            backgroundView2.setPreserveRatio(false);
            backgroundView2.setTranslateX(Constants.SCREEN_WIDTH); // Place second image to the right

            // Add ImageViews to the background container
            backgroundContainer.getChildren().addAll(backgroundView1, backgroundView2);

            // Use AnimationTimer for smooth, continuous scrolling
            final double speed = -100.0; // Pixels per second (negative for left movement)
            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;

                @Override
                public void handle(long now) {
                    if (lastUpdate == 0) {
                        lastUpdate = now;
                        return;
                    }

                    // Calculate elapsed time in seconds
                    double elapsedSeconds = (now - lastUpdate) / 1_000_000_000.0;
                    lastUpdate = now;

                    // Update positions
                    double currentX1 = backgroundView1.getTranslateX();
                    double currentX2 = backgroundView2.getTranslateX();
                    double deltaX = speed * elapsedSeconds;

                    // Move both images to the left
                    currentX1 += deltaX;
                    currentX2 += deltaX;
                    backgroundView1.setTranslateX(currentX1);
                    backgroundView2.setTranslateX(currentX2);

                    // Seamlessly reposition images to avoid gaps
                    if (currentX1 <= -Constants.SCREEN_WIDTH) {
                        backgroundView1.setTranslateX(currentX2 + Constants.SCREEN_WIDTH);
                        currentX1 = currentX2 + Constants.SCREEN_WIDTH;
                    }
                    if (currentX2 <= -Constants.SCREEN_WIDTH) {
                        backgroundView2.setTranslateX(currentX1 + Constants.SCREEN_WIDTH);
                        currentX2 = currentX1 + Constants.SCREEN_WIDTH;
                    }
                }
            };
            timer.start();
        }

        return backgroundContainer;
    }

    private Button createButtonWithImage(String imagePath) {
        Image img = ImgLoader.loadImage(imagePath);
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        Button button = new Button();
        // Put Img into button
        button.setGraphic(imageView);
        // Make button background transparent
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        return button;
    }

    private void showHighScores() {
        Map<String, Integer> scores = highScoreManager.getAllHighScores();
        StringBuilder scoreText = new StringBuilder("HIGH SCORES:\n\n");
        if (scores.isEmpty()) {
            scoreText.append("No score yet");
        } else {
            List<String> sortedLevels = new ArrayList<String>(scores.keySet());
            Collections.sort(sortedLevels);
            for (String levelId : sortedLevels) {
                String levelNumber = levelId.replace("level", "");
                scoreText.append(String.format("Level %s: %d%n", levelNumber, scores.get(levelId)));
            }
        }
        showHighScoresAlert(scoreText.toString());
    }

    // Show high score dialog
    private void showHighScoresAlert(String scoreText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText(null);
        Label label = new Label(scoreText);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }

    // Method để thay đổi text của titleLabel
    public void setTitleText(String text) {
        if (titleLabel != null) {
            titleLabel.setText(text);
        }
    }

    // Method để reset title về mặc định
    public void resetTitle() {
        if (titleLabel != null) {
            titleLabel.setText("Arkanoid");
        }
    }
}
