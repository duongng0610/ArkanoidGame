package objects;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.Constants;

public class ExpandPaddlePowerUp extends PowerUp {
    public ExpandPaddlePowerUp(double x, double y) {
        super(x, y, Constants.POWER_UP_SIZE, Constants.POWER_SPEED);

        this.view = new Rectangle(width, height, Color.LIGHTGREEN);
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.expand();
        // Tạo một thread mới để reset tốc độ sau 10 giây
        new Thread(() -> {
            try {
                Thread.sleep(10000); // 10 giây
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Phải chạy trên UI thread của JavaFX
            Platform.runLater(paddle::resetSize);
        }).start();
    }
}
