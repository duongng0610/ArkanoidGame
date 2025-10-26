package util;

import javafx.scene.image.Image;

import java.io.InputStream;

public class ImgLoader {

    public static Image loadImage(String resourcePath) {
        try {
            // find from resources file
            InputStream stream = ImgLoader.class.getResourceAsStream(resourcePath);

            if (stream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return new Image(stream);
        } catch (Exception e) {
            throw new RuntimeException("Error loading image: " + resourcePath, e);
        }
    }
}
