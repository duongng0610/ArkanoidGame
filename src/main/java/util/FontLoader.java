package util;

import javafx.scene.text.Font;

import java.io.InputStream;

public class FontLoader {
    public static Font loadFont(String resourcePath, double size) {
        try {
            // Tìm file font từ thư mục resources
            InputStream stream = FontLoader.class.getResourceAsStream(resourcePath);

            if (stream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            Font font = Font.loadFont(stream, size);
            if (font == null) {
                throw new RuntimeException("Cannot load font from: " + resourcePath);
            }

            return font;

        } catch (Exception e) {
            throw new RuntimeException("Error loading font: " + resourcePath, e);
        }
    }
}
