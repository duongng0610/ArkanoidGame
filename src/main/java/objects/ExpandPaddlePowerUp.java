package objects;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;

public class ExpandPaddlePowerUp extends PowerUp {
    private static final Image IMAGE = ImgLoader.loadImage("/assets/power_up/crusher.png");

    public ExpandPaddlePowerUp(double x, double y) {
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
