package objects;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public abstract class Brick extends GameObject {
    protected int hitPoints;
    protected int initialHitPoints;
    protected int score;
    protected boolean isDestroyed = false;
    protected Color baseColor;

    public Brick(double x, double y, double width, double height, Color color, int hitPoints, int score) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.initialHitPoints = hitPoints;
        this.score = score;
        this.baseColor = color;

        this.view = new Rectangle(width, height, color);
        this.view.setTranslateX(x);
        this.view.setTranslateY(y);
    }

    public void takeHit() {
        if (isDestroyed) {
            return;
        }

        hitPoints--;
        if (hitPoints <= 0) {
            isDestroyed = true;
        } else {
            updateVisual();
        }
    }

    protected void updateVisual() {
        double heathRatio = (double) hitPoints / initialHitPoints;
        Color fadedColor = baseColor.deriveColor(0, 1, 1, heathRatio);

        if (view instanceof Rectangle rect) {
            rect.setFill(fadedColor);
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public int getScore() {
        return score;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }
}
