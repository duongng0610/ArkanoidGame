package view;

import game.Main;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.Constants;
import util.FontLoader;
import util.ImgLoader;

public class MenuView {
    private Scene scene;
    private Main mainApp;
    private Label titleLabel;
    private AnimationTimer backgroundTimer;
    private long lastUpdate = 0;


    public MenuView(Main mainApp) {
        this.mainApp = mainApp;

        // Create the main layout with StackPane
        StackPane root = new StackPane();

        // Load background with scrolling effect
        Pane backgroundContainer = loadBackground("/assets/menu/menu.png");

        // Create VBox for buttons and title
        VBox buttonLayout = new VBox(20);
        buttonLayout.setAlignment(Pos.CENTER);

        // Load menu font
        Font menuFont = FontLoader.loadFont("/assets/menu/Skyscapers.ttf", 50);

        // Create title label for "Arkanoid"
        titleLabel = new Label("Arkanoid");
        titleLabel.setFont(menuFont);
        titleLabel.setTextFill(Color.ORANGE);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0.5, 2, 2);");
        titleLabel.setTranslateY(-20); // Move title up by 20 pixels

        // Buttons
        Button startButton = createButtonWithImage("/assets/menu/start.png");
        Button highScoresButton = createButtonWithImage("/assets/menu/score.png");
        Button exitButton = createButtonWithImage("/assets/menu/exit.png");

        startButton.setOnAction(e -> this.mainApp.startGame());
        exitButton.setOnAction(e -> this.mainApp.exitGame());
        highScoresButton.setOnAction(e -> this.mainApp.showScoreScreen());

        // Add title and buttons to buttonLayout
        buttonLayout.getChildren().addAll(titleLabel, startButton, highScoresButton, exitButton);

        // Add background and button layout to root
        root.getChildren().addAll(backgroundContainer, buttonLayout);

        scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    private Pane loadBackground(String imagePath) {
        Pane backgroundContainer = new Pane();
        Image backgroundImage = ImgLoader.loadImage(imagePath);

        if (backgroundImage != null) {
            // Create two ImageViews for seamless scrolling
            ImageView backgroundView1 = new ImageView(backgroundImage);
            backgroundView1.setFitWidth(Constants.SCREEN_WIDTH);
            backgroundView1.setFitHeight(Constants.SCREEN_HEIGHT);
            backgroundView1.setPreserveRatio(false);

            ImageView backgroundView2 = new ImageView(backgroundImage);
            backgroundView2.setFitWidth(Constants.SCREEN_WIDTH);
            backgroundView2.setFitHeight(Constants.SCREEN_HEIGHT);
            backgroundView2.setPreserveRatio(false);
            backgroundView2.setTranslateX(Constants.SCREEN_WIDTH);

            // Add ImageViews to the background container
            backgroundContainer.getChildren().addAll(backgroundView1, backgroundView2);

            // Use AnimationTimer for smooth, continuous scrolling
            final double speed = -100.0;
            backgroundTimer = new AnimationTimer() {
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
            startAnimation();
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

        // UI button effect
        button.setOnMouseEntered(e -> imageView.setOpacity(0.8));
        button.setOnMouseExited(e -> imageView.setOpacity(1.0));
        button.setOnMousePressed(e -> imageView.setOpacity(0.6));
        button.setOnMouseReleased(e -> imageView.setOpacity(0.8));

        return button;
    }

    public Scene getScene() {
        return scene;
    }

    public void startAnimation() {
        if (backgroundTimer != null) {
            backgroundTimer.start();
            lastUpdate = 0;
        }
    }

    public void stopAnimation() {
        if (backgroundTimer != null) {
            backgroundTimer.stop();
        }
    }

    // Set text for title for menu
    public void setTitleText(String text) {
        if (titleLabel != null) {
            titleLabel.setText(text);
        }
    }

    // Reset to default tile for menu
    public void resetTitle() {
        if (titleLabel != null) {
            titleLabel.setText("Arkanoid");
        }
    }
}

