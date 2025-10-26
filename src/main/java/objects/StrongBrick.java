package objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;

public class StrongBrick extends Brick {
        private static final Image IMAGE = ImgLoader.loadImage("/assets/bricks/strong_brick.png");

        public StrongBrick(double x, double y) {
            super(x, y, Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT,
                    Constants.STRONG_BRICK_HP, Constants.STRONG_BRICK_SCORE);

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
            } else {
                // blur the img after collision with ball
                double newOpacity = (double) hitPoints / initialHitPoints;
                view.setOpacity(newOpacity);
            }

        }
}
