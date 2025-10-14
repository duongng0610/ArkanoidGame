package view;

import game.Main;
import util.Constants;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MenuView {
    private Scene scene;
    private Main mainApp;

    public MenuView(Main mainApp) {
        this.mainApp = mainApp;

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");

        Button startButton = new Button("Start Game");
        Button exitButton = new Button("Exit");

        // Cải thiện giao diện nút bấm
        String buttonStyle = "-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10 20; -fx-border-color: #555; -fx-border-width: 2;";
        startButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        startButton.setOnAction(e -> this.mainApp.startGame());
        exitButton.setOnAction(e -> this.mainApp.exitGame());

        layout.getChildren().addAll(startButton, exitButton);

        scene = new Scene(layout, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}

