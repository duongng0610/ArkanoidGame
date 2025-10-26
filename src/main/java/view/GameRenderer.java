package view;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Constants;
import util.FontLoader;
import util.ImgLoader;

public class GameRenderer {
    private GraphicsContext gc;

    private static final Font gameFont = FontLoader.loadFont("/assets/game/game_font.ttf", 20);
    private static final Font messageFont = FontLoader.loadFont("/assets/game/message_font.ttf", 40);

    private static final Image fullHeart = ImgLoader.loadImage("/assets/game/full_heart.png");
    private static final Image emptyHeart = ImgLoader.loadImage("/assets/game/empty_heart.png");

    public GameRenderer(Canvas canvas) {
        this.gc = canvas.getGraphicsContext2D();
    }

    public void updateScore(int score) {
        gc.clearRect(0, 0, 200, 50);
        gc.setFill(Color.WHITE);
        gc.setFont(gameFont);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Score: " + score, 10, 30);
    }

    public void updateLives(int lives) {
        double totalWidth = Constants.INITIAL_LIVES * Constants.HEART_SIZE + (Constants.INITIAL_LIVES - 1) * Constants.HEART_GAP;
        double startX = Constants.SCREEN_WIDTH - totalWidth - Constants.HEART_PADDING;

        // Clear
        gc.clearRect(startX - 20, 0, totalWidth + Constants.HEART_PADDING * 2, 50);

        // Draw Text: Lives
        gc.setFill(Color.RED);
        gc.setFont(gameFont);
        gc.setTextAlign(TextAlignment.RIGHT);

        // Draw Img : heart
        for (int i = 0; i < Constants.INITIAL_LIVES; i++) {
            if (i < lives) {
                gc.drawImage(fullHeart, startX + i * (Constants.HEART_SIZE + Constants.HEART_GAP),
                        10, Constants.HEART_SIZE, Constants.HEART_SIZE);
            } else {
                gc.drawImage(emptyHeart, startX + i * (Constants.HEART_SIZE + Constants.HEART_GAP),
                        10, Constants.HEART_SIZE, Constants.HEART_SIZE);
            }
        }
    }

    public void showMessage(String text) {
        gc.setFill(Color.WHITE);
        gc.setFont(messageFont);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, Constants.SCREEN_WIDTH / 2.0, Constants.SCREEN_HEIGHT / 2.0);
    }

    public void hideMessage() {
        gc.clearRect(0, Constants.SCREEN_HEIGHT / 2.0 - 50, Constants.SCREEN_WIDTH, 100);
    }

}
