package view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import util.Constants;

public class GameView {
    private Scene scene;
    private Pane gamePane;
    private Canvas hudCanvas; // Draw Scores, Lives
    private GameRenderer renderer;

    public GameView() {
        gamePane = new Pane();
        setupHUD();
        scene = new Scene(gamePane, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

     public void setBackground(Image backgroundImage) {
        BackgroundSize backgroundSize = new BackgroundSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT,
                false, false, false, false);

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

        gamePane.setBackground(new Background(background));
    }

    private void setupHUD() {
        hudCanvas = new Canvas(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        gamePane.getChildren().add(hudCanvas);
        renderer = new GameRenderer(hudCanvas);
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
