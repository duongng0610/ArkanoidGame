package view;

import game.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import managers.HighScoreManager;
import util.Constants;
import util.FontLoader;
import util.ImgLoader;

public class ScoreView {
    private Scene scene;
    private Main mainApp;
    private HighScoreManager highScoreManager;

    private StackPane root;
    private VBox highScoreLayout;
    private Label highScoreValueLabel;
    private Font menuFont;
    private Font textFont;
    private Font scoreFont;

    public ScoreView(Main mainApp) {
        this.mainApp = mainApp;
        this.highScoreManager = HighScoreManager.getInstance();

        root = new StackPane();

        // Load background
        Pane backgroundContainer = loadBackground("/assets/menu/score_background.png");

        // Load fonts
        menuFont = FontLoader.loadFont("/assets/menu/Skyscapers.ttf", 50);
        textFont = FontLoader.loadFont("/assets/menu/Skyscapers.ttf", 30);
        scoreFont = FontLoader.loadFont("/assets/game/game_font.ttf", 60);

        highScoreLayout = new VBox(20);
        highScoreLayout.setAlignment(Pos.CENTER);

        Label highScoreTitleLabel = new Label("High Score");
        highScoreTitleLabel.setFont(menuFont);
        highScoreTitleLabel.setTextFill(Color.CYAN);
        highScoreTitleLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0.5, 2, 2);");

        highScoreValueLabel = new Label("0");
        highScoreValueLabel.setFont(scoreFont);
        highScoreValueLabel.setTextFill(Color.WHITE);

        Label escLabel = new Label("Press ESC to return");
        escLabel.setFont(textFont);
        escLabel.setTextFill(Color.LIGHTGRAY);

        highScoreLayout.getChildren().addAll(highScoreTitleLabel, highScoreValueLabel, escLabel);

        // Add background and layout to root
        root.getChildren().addAll(backgroundContainer, highScoreLayout);

        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        // Press ESC to return menu
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                mainApp.showMenu();
            }
        });
    }

    private Pane loadBackground(String imagePath) {
        Pane backgroundContainer = new Pane();
        Image backgroundImage = ImgLoader.loadImage(imagePath);

        if (backgroundImage != null) {
            ImageView backgroundView1 = new ImageView(backgroundImage);
            backgroundView1.setFitWidth(Constants.SCREEN_WIDTH);
            backgroundView1.setFitHeight(Constants.SCREEN_HEIGHT);
            backgroundView1.setPreserveRatio(false);
            backgroundContainer.getChildren().add(backgroundView1);
        }
        return backgroundContainer;
    }

    public void updateScore() {
        int score = highScoreManager.getHighScore();
        highScoreValueLabel.setText(String.valueOf(score));
    }

    public Scene getScene() {
        return scene;
    }

}

