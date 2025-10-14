package objects;

import javafx.geometry.Bounds;
import util.Constants;

import javax.swing.*;
import java.awt.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Paddle extends GameObject {
    public Paddle(double x, double y) {
        super(x, y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
        this.view = new Rectangle(width, height, Color.CYAN);
        updateViewPosition();
    }

    public void moveLeft() {
        if (x > 0) {
            x -= Constants.PADDLE_SPEED;
        }
        updateViewPosition();
    }

    public void moveRight() {
        if (x + width < Constants.SCREEN_WIDTH) {
            x += Constants.PADDLE_SPEED;
        }
        updateViewPosition();
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

    // ExpandPaddlePowerUp
    public void expand() {
        this.width = Constants.PADDLE_EXPAND_WIDTH;

        if (x + width > Constants.SCREEN_WIDTH) {
            x = Constants.SCREEN_WIDTH - width;
        }
        updateViewPosition();
    }

    private void updateViewPosition() {
        if (view instanceof Rectangle rect) {
            rect.setWidth(width);
        }

        view.setTranslateX(x);
        view.setTranslateY(y);
    }

    public void resetSize() {
        this.width = Constants.PADDLE_WIDTH;
        updateViewPosition();
    }

}
