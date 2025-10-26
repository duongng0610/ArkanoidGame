package util;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;


public class SoundLoader {
    private static URL getResourceUrl(String resourcePath) {
        URL url = SoundLoader.class.getResource(resourcePath);
        if (url == null) {
            System.err.println("Resource not found: " + resourcePath);
        }
        return url;
    }

    public static AudioClip loadClip(String path) {
        URL url = getResourceUrl(path);
        if (url != null) {
            try {
                return new AudioClip(url.toExternalForm());
            } catch (Exception e) {
                throw new RuntimeException("Cannot load audio clip from: " + url);
            }
        }
        return null;
    }


    public static MediaPlayer loadMusic(String path) {
        URL url = getResourceUrl(path);
        if (url != null) {
            try {
                Media media = new Media(url.toExternalForm());
                return new MediaPlayer(media);
            } catch (Exception e) {
                throw new RuntimeException("Cannot load media player from: " + url);
            }
        }
        return null;
    }
}
