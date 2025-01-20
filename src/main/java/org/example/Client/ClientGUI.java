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
            client = new Client("localhost", 59899);

            client.setMessageHandler(this::onServerMessage);

            lobbyController = new LobbyController(client);

            Scene scene = new Scene(lobbyController.getRoot(), 600, 400);
            primaryStage.setTitle("Chinese Checkers Lobby");
            primaryStage.setScene(scene);
            primaryStage.show();

            client.startListening();

            client.sendToServer("list");

        } catch (IOException e) {
            Label errorLabel = new Label("Failed to connect to server: " + e.getMessage());
            Scene scene = new Scene(errorLabel, 400, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    private void onServerMessage(String line) {
        Platform.runLater(() -> {
            lobbyController.handleServerMessage(line);
        });
    }
}
