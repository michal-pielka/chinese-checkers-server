package org.example.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A JavaFX Application that connects to the server, displays a lobby,
 * and transitions to an in-game UI when the game starts.
 */
public class ClientGUI extends Application {

    /**
     * The Client instance for sending commands to the server.
     */
    private Client client;

    /**
     * The controller for the lobby UI.
     */
    private LobbyController lobbyController;

    /**
     * Initializes the JavaFX stage, sets up the Client connection,
     * and loads the LobbyController UI.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            client = new Client("localhost", 59899);

            client.setMessageHandler(this::onServerMessage);

            lobbyController = new LobbyController(client);

            Scene scene = new Scene(lobbyController.getRoot(), 600, 400);
            primaryStage.setTitle("Chinese Checkers Lobby");
            primaryStage.setScene(scene);
            primaryStage.show();

            client.startListening();

            // For demonstration, immediately request a list of games
            client.sendToServer("list");

        } catch (IOException e) {
            Label errorLabel = new Label("Failed to connect to server: " + e.getMessage());
            Scene scene = new Scene(errorLabel, 400, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Called whenever a message is received from the server.
     * Delegates handling to the LobbyController on the JavaFX thread.
     *
     * @param line The message from the server.
     */
    private void onServerMessage(String line) {
        Platform.runLater(() -> {
            lobbyController.handleServerMessage(line);
        });
    }
}
