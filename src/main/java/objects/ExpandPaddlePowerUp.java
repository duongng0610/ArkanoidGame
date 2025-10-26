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

    // using power
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        paddle.expand();

        // stop size up paddle after a time
        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(paddle::resetSize);
        }).start();
    }
}
