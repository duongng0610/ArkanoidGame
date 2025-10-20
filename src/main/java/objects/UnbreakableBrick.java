package objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;

public class UnbreakableBrick extends Brick {
    private static final Image IMAGE = new Image("/assets/bricks/unbreakable_brick.png");

    public UnbreakableBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT,
                99999, 0);

        this.view = new ImageView(IMAGE);
        this.view.setFitWidth(this.width);
        this.view.setFitHeight(this.height);
        this.view.setLayoutX(this.x);
        this.view.setLayoutY(this.y);
    }

    @Override
    public void takeHit() {

    }
}
