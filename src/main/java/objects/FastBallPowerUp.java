package objects;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;

/**
 * A power-up that temporarily increases the ball's speed.
 */
public class FastBallPowerUp extends PowerUp {
    private static final Image IMAGE = ImgLoader.loadImage("/assets/power_up/speeder.png");

    public FastBallPowerUp(double x, double y) {
        super(x, y, Constants.POWER_SPEED);

        // load Image
        this.view = new ImageView(IMAGE);

        // set size
        this.view.setFitWidth(Constants.POWER_UP_SIZE);
        this.view.setFitHeight(Constants.POWER_UP_SIZE);

        // set position
        this.view.setLayoutX(x);
        this.view.setLayoutY(y);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        ball.multiplySpeed(1.25);

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