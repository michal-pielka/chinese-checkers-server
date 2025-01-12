package org.example.Gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX Application class for the Chinese Checkers Lobby GUI.
 */
public class LobbyApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/lobby.fxml"));
            Parent root = loader.load();

            // Set up the scene
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            // Configure the stage
            primaryStage.setTitle("Chinese Checkers Lobby");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            // Initialize the controller (if needed)
            LobbyController controller = loader.getController();
            controller.initialize();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the Lobby window.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
