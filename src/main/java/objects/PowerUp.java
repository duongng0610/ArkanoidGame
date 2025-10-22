package objects;

import javafx.geometry.Bounds;
import util.Constants;

public abstract class PowerUp extends MovableObject {
    private boolean isCollected = false;

    public PowerUp(double x, double y, double speed) {
        super(x, y, Constants.POWER_UP_SIZE, Constants.POWER_UP_SIZE, 0, speed);
    }

    public void collect() {
        isCollected = true;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

    public abstract void applyEffect(Paddle paddle, Ball ball);
}
