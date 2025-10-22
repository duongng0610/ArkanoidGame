package view;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Constants;

public class GameRenderer {
    private GraphicsContext gc;
    private Font gameFont = Font.loadFont(getClass().getResourceAsStream("/assets/game/custom_font.ttf"), 20);
    private Font messageFont = Font.font("Verdana", 40);

    // Giả sử bạn có hình ảnh cho mạng sống đầy (full) và mạng sống đã mất (empty)
    // Đặt đường dẫn đến file hình ảnh (có thể là resource trong project, ví dụ: "/images/full_heart.png")
    private Image fullHeart = new Image(getClass().getResourceAsStream("/assets/game/full_heart.png"));
    private Image emptyHeart = new Image(getClass().getResourceAsStream("/assets/game/empty_heart.png"));

    // Giả sử số mạng sống tối đa là 3 (bạn có thể thay đổi con số này)
    private static final int MAX_LIVES = 3;

    // Kích thước hình ảnh (điều chỉnh theo nhu cầu)
    private static final double HEART_SIZE = 30;

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
        // Xóa khu vực cũ
        gc.clearRect(Constants.SCREEN_WIDTH - (MAX_LIVES * (HEART_SIZE + 5)) - 50, 0, (MAX_LIVES * (HEART_SIZE + 5)) + 50, 50);

        // Vẽ chữ "Lives:" nếu muốn giữ lại, hoặc bỏ nếu chỉ muốn hình ảnh
        gc.setFill(Color.RED);
        gc.setFont(gameFont);
        gc.setTextAlign(TextAlignment.RIGHT);

        // Vẽ hình ảnh cho mạng sống
        double x = Constants.SCREEN_WIDTH - (MAX_LIVES * (HEART_SIZE + 5)); // Bắt đầu từ phải, cách lề một chút
        for (int i = 0; i < MAX_LIVES; i++) {
            if (i < lives) {
                gc.drawImage(fullHeart, x + i * (HEART_SIZE + 5), 10, HEART_SIZE, HEART_SIZE);
            } else {
                gc.drawImage(emptyHeart, x + i * (HEART_SIZE + 5), 10, HEART_SIZE, HEART_SIZE);
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
