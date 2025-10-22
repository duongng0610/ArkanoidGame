package game;

import javafx.application.Platform;
import javafx.application.Application;
import javafx.stage.Stage;
import view.GameView;
import view.MenuView;

public class Main extends Application {
    private Stage primaryStage;
    private GameManager gameManager;
    private GameView gameView;
    private MenuView menuView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Arkanoid");

        // start
        menuView = new MenuView(this);
        primaryStage.setScene(menuView.getScene());

        primaryStage.show();
    }

<<<<<<< HEAD
    public void startGame() {
        gameView = new GameView();  // Khởi tạo gameView ở đây để tránh null
=======

    public void startGame() {
        GameView gameView = new GameView();
>>>>>>> 2b193a40daaa8252b9a9c06e32c3819744377e50

        gameManager = new GameManager(gameView, this);

        primaryStage.setScene(gameView.getScene());

        gameManager.start();
    }

    public void showMenu() {
        primaryStage.setScene(menuView.getScene());
    }

    public void exitGame() {
        Platform.exit();
    }

<<<<<<< HEAD
    // Method để set title menu
    public void setMenuTitle(String text) {
        if (menuView != null) {
            menuView.setTitleText(text);
        }
    }

    // Method để reset title menu
    public void resetMenuTitle() {
        if (menuView != null) {
            menuView.resetTitle();
        }
    }
=======
>>>>>>> 2b193a40daaa8252b9a9c06e32c3819744377e50
}
