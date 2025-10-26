package objects;

import javafx.geometry.Bounds;

public abstract class Brick extends GameObject {
    protected int hitPoints;
    protected int initialHitPoints;
    protected int score;
    protected boolean isDestroyed = false;

    public Brick(double x, double y, double width, double height, int hitPoints, int score) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.initialHitPoints = hitPoints;
        this.score = score;
    }

    public abstract void takeHit();

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public int getScore() {
        return score;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

    public void setDestroyed(boolean b) {
        this.isDestroyed = b;
    }
}