package util;

import javafx.scene.image.Image;

import java.io.InputStream;

public class ImgLoader {

    public static Image loadImage(String resourcePath) {
        try {
            InputStream stream = ImgLoader.class.getResourceAsStream(resourcePath);

            if (stream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return new Image(stream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load image: " + resourcePath, e);
        }
    }
}
