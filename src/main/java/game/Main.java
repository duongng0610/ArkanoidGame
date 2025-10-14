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

    public void startGame() {
        GameView gameView = new GameView();

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

}
