package util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

// Lớp này phải implements Serializable để có thể được ghi vào file
public class ScoreEntry implements Serializable {
    // serialVersionUID giúp đảm bảo tương thích khi bạn thay đổi lớp này sau này
    private static final long serialVersionUID = 1L;

    private final int score;
    private final String timestamp;

    public ScoreEntry(int score) {
        this.score = score;
        // Tự động lấy ngày giờ hiện tại khi một điểm mới được tạo
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.timestamp = formatter.format(new Date());
    }

    public int getScore() {
        return score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Score: %-10d | Date: %s", score, timestamp);
    }
}