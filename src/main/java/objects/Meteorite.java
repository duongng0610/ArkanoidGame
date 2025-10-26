package objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Constants;
import util.ImgLoader;

public class Meteorite extends MovableObject {

    private boolean isDestroyed;
    private static final Image IMAGE = ImgLoader.loadImage("/assets/power_up/meteorite.png");

    public Meteorite(double x, double y) {
        super(x, y, Constants.METEORITE_WIDTH, Constants.METEORITE_HEIGHT, 0, Constants.METEORITE_SPEED);

        // load Image
        this.view = new ImageView(IMAGE);

        // set size
        this.view.setFitWidth(this.width);
        this.view.setFitHeight(this.height);

        // set position
        this.view.setLayoutX(this.x);
        this.view.setLayoutY(this.y);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}

