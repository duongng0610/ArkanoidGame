package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class HighScoreManager {
    private static final HighScoreManager INSTANCE = new HighScoreManager();
    private static final String HIGHSCORE_FILE = "highscore.dat";
    private static final String HIGHSCORE_KEY = "highScore";

    private int highScore;
    private Properties properties;


    private HighScoreManager() {
        this.properties = new Properties();
        this.highScore = 0;
        loadHighScore();
    }

    public static HighScoreManager getInstance() {
        return INSTANCE;
    }

    // Load data
    private void loadHighScore() {
        File file = new File(HIGHSCORE_FILE);
        if (!file.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        String scoreStr = properties.getProperty(HIGHSCORE_KEY);
        if (scoreStr != null) {
            try {
                this.highScore = Integer.parseInt(scoreStr);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse high score: " + e.getMessage());
                this.highScore = 0;
            }
        }
    }

    private void saveHighScore() {
        try (FileOutputStream fos = new FileOutputStream(HIGHSCORE_FILE)) {
            properties.store(fos, "High Score");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public void updateHighScore(int newScore) {
        if (newScore > highScore) {
            this.highScore = newScore;
            properties.setProperty(HIGHSCORE_KEY, String.valueOf(newScore));
            saveHighScore();
        }
    }
}