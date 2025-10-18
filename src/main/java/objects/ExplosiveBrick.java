package objects;

import javafx.scene.paint.Color;
import util.Constants;

public class ExplosiveBrick extends Brick {
    public ExplosiveBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT, Color.YELLOW, 
                Constants.EXPLOSIVE_BRICK_HP, Constants.EXPLOSIVE_BRICK_SCORE);
    }
}
