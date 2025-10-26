package objects;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;


public class Paddle extends GameObject {
    private static final Image PADDLE_NORMAL_IMG = ImgLoader.loadImage("/assets/paddle/paddle.png");
    private static final Image PADDLE_EXPAND_IMG = ImgLoader.loadImage("/assets/paddle/paddle_x2.png");

    public Paddle(double x, double y) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);

        // load Image
        this.view = new ImageView(PADDLE_NORMAL_IMG);

        // set size
        this.view.setFitWidth(this.width);
        this.view.setFitHeight(this.height);

        // set position
        this.view.setLayoutX(x);
        this.view.setLayoutY(y);
    }

    public void moveLeft() {
        if (x > 0) {
            x -= Constants.PADDLE_SPEED;
            view.setLayoutX(x);
        }
    }

    public void moveRight() {
        if (x + width < Constants.SCREEN_WIDTH) {
            x += Constants.PADDLE_SPEED;
            view.setLayoutX(x);
        }
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

    // ExpandPaddlePowerUp
    public void expand() {
        this.width = Constants.PADDLE_EXPAND_WIDTH;

        this.view.setImage(PADDLE_EXPAND_IMG);

        this.view.setFitWidth(this.width);
    }

    // reset to default size
    public void resetSize() {
        this.width = Constants.PADDLE_WIDTH;

        this.view.setImage(PADDLE_NORMAL_IMG);

        this.view.setFitWidth(this.width);
    }

}
