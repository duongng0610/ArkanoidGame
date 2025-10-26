package objects;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;

public class Ball extends MovableObject {

    private final double originalSpeed;
    private final static Image IMAGE = ImgLoader.loadImage("/assets/ball/ball.png");

    public Ball (double x, double y) {
        super(x, y, Constants.BALL_RADIUS * 2, Constants.BALL_RADIUS * 2,
                Constants.BALL_SPEED, -Constants.BALL_SPEED);

        this.originalSpeed = Constants.BALL_SPEED;

        // load Image
        this.view = new ImageView(IMAGE);

        // set size
        this.view.setFitHeight(this.width);
        this.view.setFitHeight(this.height);

        // set position
        this.view.setLayoutX(x);
        this.view.setLayoutY(y);

        // set velocity
        this.dx = Constants.BALL_SPEED;
        this.dy = -Constants.BALL_SPEED;

    }

    public void bounceY() {
        dy = -dy;
    }

    public void bounceX() {
        dx = -dx;
    }

    // reset ball to default status
    public void reset(double paddleX, double paddleWidth) {
        setX(paddleX + (paddleWidth / 2) - Constants.BALL_RADIUS);
        setY(Constants.PADDLE_START_Y - Constants.BALL_RADIUS * 2);

        this.dx = Constants.BALL_SPEED;
        this.dy = -Constants.BALL_SPEED;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

    public void multiplySpeed(double factor) {
        dx *= factor;
        dy *= factor;
    }

    // use normalize vector to reset originSpeed after speedup
    public void resetSpeed() {
        double currentSpeed = Math.sqrt(dx * dx + dy * dy);
        if (currentSpeed > 0) {
            dx = (dx / currentSpeed) * originalSpeed * Math.sqrt(2);
            dy = (dy / currentSpeed) * originalSpeed * Math.sqrt(2);
        }
    }

}
