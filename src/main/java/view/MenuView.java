package view;

import game.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import managers.HighScoreManager;
import util.Constants;
import util.ImgLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MenuView {
    private Scene scene;
    private Main mainApp;
    private HighScoreManager highScoreManager;

    public MenuView(Main mainApp) {
        this.mainApp = mainApp;
        this.highScoreManager = HighScoreManager.getInstance();

        // Background of Menu
        VBox menu = loadBackground("/assets/menu/menu.png");

        // Button
        Button startButton = createButtonWithImage("/assets/menu/start.png");
        Button highScoresButton = createButtonWithImage("/assets/menu/score.png");
        Button exitButton = createButtonWithImage("/assets/menu/exit.png");

        startButton.setOnAction(e -> this.mainApp.startGame());
        exitButton.setOnAction(e -> this.mainApp.exitGame());
        highScoresButton.setOnAction(e -> showHighScores());

        menu.getChildren().addAll(startButton,highScoresButton, exitButton);

        scene = new Scene(menu, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    private VBox loadBackground(String imagePath) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        if (imagePath != null) {
            layout.setStyle(
                    "-fx-background-image: url('" + imagePath + "'); " +
                            "-fx-background-size: cover;"
            );
        } else {
            layout.setStyle("-fx-background-color: black;");
        }

        return layout;
    }

    private Button createButtonWithImage(String imagePath) {
        Image img = ImgLoader.loadImage(imagePath);

        ImageView imageView = new ImageView(img);

        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        Button button = new Button();
        // put Img into button
        button.setGraphic(imageView);

        // blur the background of button
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
}

