package org.example.Gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the Lobby Window.
 */
public class LobbyController {

    @FXML
    private ListView<String> gamesListView;

    @FXML
    private Button createLobbyButton;

    @FXML
    private Button refreshButton;

    private ObservableList<String> gamesList;

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    public void initialize() {
        gamesList = FXCollections.observableArrayList();
        gamesListView.setItems(gamesList);

        // Load mock data for now
        loadMockGames();
    }

    /**
     * Handles the Create Lobby button action.
     */
    @FXML
    private void handleCreateLobby() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Lobby");
        dialog.setHeaderText("Create a New Game Lobby");
        dialog.setContentText("Enter Lobby Name:");

        dialog.showAndWait().ifPresent(lobbyName -> {
            if (lobbyName.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Lobby name cannot be empty.");
            } else {
                // TODO: Send create lobby command to the server via Client
                // Example: client.createLobby(lobbyName);

                // For demonstration, add it to the list
                gamesList.add(lobbyName + " - Waiting for players");
                showAlert(Alert.AlertType.INFORMATION, "Lobby '" + lobbyName + "' created successfully!");
            }
        });
    }

    /**
     * Handles the Refresh button action.
     */
    @FXML
    private void handleRefresh() {
        // TODO: Send refresh command to the server via Client
        // Example: client.listGames();

        // For demonstration, reload mock data
        loadMockGames();
        showAlert(Alert.AlertType.INFORMATION, "Game list refreshed.");
    }

    /**
     * Loads mock games into the ListView. Replace this with actual server data.
     */
    private void loadMockGames() {
        Platform.runLater(() -> {
            gamesList.clear();
            gamesList.addAll(
                "Lobby1 - 2/6 players",
                "Lobby2 - 3/4 players",
                "Lobby3 - 1/2 players"
            );
        });
    }

    /**
     * Displays an alert dialog with the specified type and message.
     *
     * @param type    The type of alert.
     * @param message The message to display.
     */
    private void showAlert(Alert.AlertType type, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, message, ButtonType.OK);
            alert.showAndWait();
        });
    }
}
