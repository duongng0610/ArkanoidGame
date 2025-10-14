package objects;

import util.Constants;
import javafx.scene.paint.Color;

public class NormalBrick extends Brick {
    public NormalBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Color.ORANGE,
                Constants.NORMAL_BRICK_HP, Constants.NORMAL_BRICK_SCORE);
    }
}
