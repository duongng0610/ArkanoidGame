package objects;

import javafx.scene.paint.Color;
import util.Constants;

public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Color.DARKGRAY,
                9999, 0);
    }

    @Override
    public void takeHit() {

    }
}
