package objects;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import util.Constants;
import javafx.scene.paint.Color;

/**
 * A power-up that temporarily increases the ball's speed.
 */
public class FastBallPowerUp extends PowerUp {
    public FastBallPowerUp(double x, double y) {
        super(x, y, Constants.POWER_UP_SIZE, Constants.POWER_SPEED);
        // Sau này sẽ thay bằng ảnh
        this.view = new Rectangle(width, height, Color.YELLOW);
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.multiplySpeed(1.5);

        // Tạo một thread mới để reset tốc độ sau 10 giây
        new Thread(() -> {
            try {
                Thread.sleep(10000); // 10 giây
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Phải chạy trên UI thread của JavaFX
            Platform.runLater(ball::resetSpeed);
        }).start();
    }
}