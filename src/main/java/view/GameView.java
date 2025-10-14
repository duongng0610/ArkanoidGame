package view;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import util.Constants;

import javax.swing.*;
import java.awt.*;

public class GameView {
    private Scene scene;
    private Pane gamePane;
    private Canvas hudCanvas; // Draw Scores, Lives
    private GameRenderer renderer;
    public GameView() {
        // main space
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");

        hudCanvas = new Canvas(800, 600);
        gamePane.getChildren().add(hudCanvas);

        renderer = new GameRenderer(hudCanvas);

        scene = new Scene(gamePane, 800, 600, Color.BLACK);
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public Scene getScene() {
        return scene;
    }

    public GameRenderer getRenderer() {
        return renderer;
    }
}
