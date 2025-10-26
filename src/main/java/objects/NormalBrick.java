package objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;

public class NormalBrick extends Brick {
    private static final Image IMAGE = ImgLoader.loadImage("/assets/bricks/normal_brick.png");
    public NormalBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT,
                Constants.NORMAL_BRICK_HP, Constants.NORMAL_BRICK_SCORE);

        // load Image
        this.view = new ImageView(IMAGE);

        // set size
        this.view.setFitWidth(this.width);
        this.view.setFitHeight(this.height);

        // set position
        this.view.setLayoutX(this.x);
        this.view.setLayoutY(this.y);


    }

    @Override
    public void takeHit() {
        if (isDestroyed) {
            return;
        }

        hitPoints--;
        if (hitPoints <= 0) {
            isDestroyed = true;
        }
    }
}
