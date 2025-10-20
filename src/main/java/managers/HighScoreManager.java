package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HighScoreManager {
    private static final HighScoreManager INSTANCE = new HighScoreManager();
    private static final String HIGHSCORE_FILE = "level_scores.dat";
    private static final String SECRET_KEY = "MySecretKey123*";

    private Map<String, Integer> verifiedScores;
    private Properties properties;


    private HighScoreManager() {
        this.properties = new Properties();
        this.verifiedScores = new HashMap<>();
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

        for (String key : properties.stringPropertyNames()) {
            if (key.endsWith("_score")) {
                String levelId = key.replace("_score", "");
                String scoreStr = properties.getProperty(key);
                String hashFromfile = properties.getProperty(levelId + "_hash");

                if (hashFromfile != null && verifyHash(scoreStr, hashFromfile)) {
                    verifiedScores.put(levelId, Integer.parseInt(scoreStr));
                } else {
                    System.out.println("Data is not suitable");
                }
            }
        }
    }

    private void saveHighScore() {
        try (FileOutputStream fos = new FileOutputStream(HIGHSCORE_FILE)) {
            properties.store(fos, "Per-Level High Scores");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getHighScore(String levelId) {
        return verifiedScores.getOrDefault(levelId, 0);
    }

    // For Menu to show all high scores in levels
    public Map<String, Integer> getAllHighScores() {
        return new HashMap<>(verifiedScores);
    }

    public void updateHighScore(String levelId, int newScore) {
        int currentHighScore = getHighScore(levelId);

        if (newScore > currentHighScore) {
            verifiedScores.put(levelId, newScore);
            String newHash = caculateHash(String.valueOf(newScore));

            properties.setProperty(levelId + "_score", String.valueOf(newScore));
            properties.setProperty(levelId + "_hash", newHash);

            saveHighScore();
        }
    }

    // Calculate the hash by algorithm SHA 256
    private String caculateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String textToHash = data + SECRET_KEY;
            byte[] hashBytes = digest.digest(textToHash.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Check the cheat when data in file is changed
    private boolean verifyHash(String data, String hashFromFile) {
        String newlyCalculateHash = caculateHash(data);
        return newlyCalculateHash.equals(hashFromFile);
    }
}