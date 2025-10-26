package game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import managers.SoundManager;
import view.GameView;
import view.MenuView;
import view.ScoreView;

public class Main extends Application {
    private Stage primaryStage;
    private GameManager gameManager;
    private GameView gameView;
    private MenuView menuView;
    private ScoreView scoreView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Arkanoid");

        // start
        menuView = new MenuView(this);
        scoreView = new ScoreView(this);
        primaryStage.setScene(menuView.getScene());

        // run menu background animation
        menuView.startAnimation();

        // play background music
        SoundManager.getInstance().playMusic();

        primaryStage.show();
    }


    public void startGame() {
        // stop menu background animation
        menuView.stopAnimation();

        GameView gameView = new GameView();
        gameManager = new GameManager(gameView, this);

        primaryStage.setScene(gameView.getScene());
        gameManager.start();
    }

    public void showMenu() {
        primaryStage.setScene(menuView.getScene());

        // run menu background animation
        menuView.startAnimation();

        // play background music
        SoundManager.getInstance().playMusic();
    }

    public void showScoreScreen() {
        // stop menu background animation
        menuView.stopAnimation();

        scoreView.updateScore();
        primaryStage.setScene(scoreView.getScene());
    }

    public void exitGame() {
        // stop background music before exit
        SoundManager.getInstance().stopMusic();

        Platform.exit();
    }

    // Set text for title
    public void setMenuTitle(String text) {
        if (menuView != null) {
            menuView.setTitleText(text);
        }
    }

    // Reset to default title
    public void resetMenuTitle() {
        if (menuView != null) {
            menuView.resetTitle();
        }
    }

}
