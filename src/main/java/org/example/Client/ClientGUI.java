package org.example.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {

    private Client client;
    private LobbyController lobbyController;

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1) Create and connect the underlying Client
            client = new Client("localhost", 59899);

            // 2) Provide a callback for incoming server messages
            client.setMessageHandler(this::onServerMessage);

            // 3) Build the Lobby GUI
            lobbyController = new LobbyController(client);

            // 4) Show the Lobby GUI as our main scene
            Scene scene = new Scene(lobbyController.getRoot(), 600, 400);
            primaryStage.setTitle("Chinese Checkers Lobby");
            primaryStage.setScene(scene);
            primaryStage.show();

            // 5) Start reading from the server
            client.startListening();

            // 6) Optionally request the game list right away
            client.sendToServer("list");

        } catch (IOException e) {
            // If we cannot connect, show an error label
            Label errorLabel = new Label("Failed to connect to server: " + e.getMessage());
            Scene scene = new Scene(errorLabel, 400, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    private void onServerMessage(String line) {
        // Switch to JavaFX thread
        Platform.runLater(() -> {
            lobbyController.handleServerMessage(line);
        });
    }
}
