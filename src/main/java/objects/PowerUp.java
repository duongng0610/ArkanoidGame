package objects;

import javafx.geometry.Bounds;

public abstract class PowerUp extends MovableObject {
    private boolean isCollected = false;

    public PowerUp(double x, double y, double size, double speed) {
        super(x, y, size, size, 0, speed);
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

    public abstract void applyEffect(Paddle paddle);
}
