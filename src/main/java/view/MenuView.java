package view;

import game.Main;
import javafx.scene.control.Alert;
import managers.HighScoreManager;
import util.Constants;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class MenuView {
    private Scene scene;
    private Main mainApp;
    private HighScoreManager highScoreManager;

    public MenuView(Main mainApp) {
        this.mainApp = mainApp;
        this.highScoreManager = new HighScoreManager();

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");

        Button startButton = new Button("Start Game");
        Button highScoresButton = new Button("High Scores");
        Button exitButton = new Button("Exit");

        // Cải thiện giao diện nút bấm
        String buttonStyle = "-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10 20; -fx-border-color: #555; -fx-border-width: 2;";
        startButton.setStyle(buttonStyle);
        highScoresButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        startButton.setOnAction(e -> this.mainApp.startGame());
        exitButton.setOnAction(e -> this.mainApp.exitGame());
        highScoresButton.setOnAction(e -> showHighScores());

        layout.getChildren().addAll(startButton,highScoresButton, exitButton);

        scene = new Scene(layout, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    private void showHighScores() {
        HighScoreManager highScoreManager = new HighScoreManager();
        List<HighScoreManager.ScoreEntry> scores = highScoreManager.getScores();

        StringBuilder scoreText = new StringBuilder("Top 5 High Scores:\n\n");
        if (scores.isEmpty()) {
            scoreText.append("No scores yet!");
        } else {
            int rank = 1;
            for (HighScoreManager.ScoreEntry entry : scores) {
                scoreText.append(rank++).append(". ").append(entry.getName())
                        .append(" - ").append(entry.getScore()).append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(new javafx.scene.control.Label(scoreText.toString()));
        alert.showAndWait();
    }

    public Scene getScene() {
        return scene;
    }
}

