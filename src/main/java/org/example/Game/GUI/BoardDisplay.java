package org.example.Game.GUI;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.example.Game.Board.Board;

public class BoardDisplay  extends Application {
    private static Board board;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        CircleManager circleManager = new CircleManager();
        circleManager.initializeCircles(board);

        for(Circle circle : circleManager.getCircles()) {
            pane.getChildren().add(circle);
        }

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void launchBoardDisplay(Board board) {
       BoardDisplay.board=board;

        launch();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
