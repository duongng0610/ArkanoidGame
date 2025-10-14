package objects;

import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import util.Constants;
import javafx.scene.paint.Color;


import java.awt.*;

public class Ball extends MovableObject {

    public Ball (double x, double y) {
        super(x, y, Constants.BALL_RADIUS * 2, Constants.BALL_RADIUS * 2,
                Constants.BALL_SPEED, -Constants.BALL_SPEED);

        this.view = new Circle(Constants.BALL_RADIUS, Color.WHITE);
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }

    public void bounceY() {
        dy = -dy;
    }

    public void bounceX() {
        dx = -dx;
    }

    public void reset(double paddleX, double paddleWidth) {
        setX(paddleX + (paddleWidth / 2) - Constants.BALL_RADIUS);
        setY(Constants.PADDLE_START_Y - Constants.BALL_RADIUS * 2);

        this.dx = Constants.BALL_SPEED;
        this.dy = -Constants.BALL_SPEED;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }
}
