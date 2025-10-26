package objects;

public abstract class MovableObject extends GameObject{
    protected double dx, dy;

    public MovableObject(double x, double y, double width, double height, double dx, double dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
        view.setLayoutX(x);
        view.setLayoutY(y);
    }


    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}
