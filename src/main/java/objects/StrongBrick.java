package objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.Constants;

public class StrongBrick extends Brick {
    public StrongBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Color.STEELBLUE,
                Constants.STRONG_BRICK_HP, Constants.STRONG_BRICK_SCORE);
    }

}
