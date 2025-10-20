package objects;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public abstract class GameObject {
    protected double x, y;
    protected double width, height;
    protected ImageView view;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Getter & Setter

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Node getView() {
        return view;
    }

    public Bounds getBounds() {
        return view.getBoundsInParent();
    }

}
