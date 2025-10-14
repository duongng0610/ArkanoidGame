package objects;

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
    public void applyEffect(Paddle paddle) {
        paddle.expand();
    }
}
