package view;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import util.Constants;

public class GameRenderer {
   private GraphicsContext gc;
   private Font gameFont = Font.font("Verdana", 20);
   private Font messageFont = Font.font("Verdana", 40);

   public GameRenderer(Canvas canvas) {
       this.gc = canvas.getGraphicsContext2D();
   }

   public void updateScore(int score) {
       gc.clearRect(0, 0, 200, 50);
       gc.setFill(Color.WHITE);
       gc.setFont(gameFont);
       gc.setTextAlign(TextAlignment.LEFT);
       gc.fillText("Score: " + score, 10, 30);
   }

    public void updateLives(int lives) {
       gc.clearRect(Constants.SCREEN_WIDTH - 150, 0, 150, 50);
       gc.setFill(Color.RED);
       gc.setFont(gameFont);
       gc.setTextAlign(TextAlignment.RIGHT);
       gc.fillText("Lives: " + lives, Constants.SCREEN_WIDTH - 10, 30);
   }

   public void showMessage(String text) {
       gc.setFill(Color.WHITE);
       gc.setFont(messageFont);
       gc.setTextAlign(TextAlignment.CENTER);
       gc.setTextBaseline(VPos.CENTER);
       gc.fillText(text, Constants.SCREEN_WIDTH / 2.0, Constants.SCREEN_HEIGHT / 2.0);
   }

   public void hideMessage() {
       gc.clearRect(0, Constants.SCREEN_HEIGHT / 2.0 - 50, Constants.SCREEN_WIDTH, 100);
   }
}
