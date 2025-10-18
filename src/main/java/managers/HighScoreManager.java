package managers;

import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String HIGHSCORE_FILE = "highscores.dat";
    private List<ScoreEntry> scores;
    private static final int MAX_SCORES = 10; // Giới hạn chỉ lưu 10 điểm cao nhất

    // Lớp nội bộ để lưu cặp tên-điểm
    public static class ScoreEntry implements Serializable {
        private final String name;
        private final int score;
        public ScoreEntry(String name, int score) { this.name = name; this.score = score; }
        public String getName() { return name; }
        public int getScore() { return score; }
    }

    public List<ScoreEntry> loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGHSCORE_FILE))) {
            return (List<ScoreEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Đây là lần đầu chạy game, file chưa tồn tại, trả về danh sách rỗng là đúng.
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
            return new ArrayList<>(); // Trả về rỗng nếu có lỗi
        }
    }

    private void saveScores(List<ScoreEntry> scores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGHSCORE_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }


    public HighScoreManager() {
        scores = loadScores();
    }

    public List<ScoreEntry> getScores() { return scores; }

    public void addScore(String name, int newScore) {
        if (newScore <= 0) return; // Không lưu điểm 0

        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(name, newScore));

        // Sắp xếp danh sách giảm dần theo điểm
        scores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        // Chỉ giữ lại 10 điểm cao nhất
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        saveScores(scores);
        System.out.println("High score saved: " + newScore);
    }

    // Phương thức saveScores và loadScores sử dụng ObjectOutputStream/ObjectInputStream
    // (Như code mẫu đã cung cấp trước đó)
}

