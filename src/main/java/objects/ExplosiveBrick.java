package objects;


import util.ImgLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;

public class ExplosiveBrick extends Brick {
    private static final Image IMAGE = ImgLoader.loadImage("/assets/bricks/explosive_brick.png");

    public ExplosiveBrick(double x, double y) {
        super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT,
                Constants.EXPLOSIVE_BRICK_HP, Constants.EXPLOSIVE_BRICK_SCORE);

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
