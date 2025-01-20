package org.example.Client;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class LobbyController {

    private final Client client;
    private final BorderPane root;

    private final ListView<String> gamesListView;
    private final TextArea serverMessagesArea;
    private final Button createButton;
    private final Button joinButton;
    private final Button refreshButton;

    // We'll keep track of lines we receive that start with "Available Games:"
    private final List<String> games = new ArrayList<>();

    // Flow states for the creation/join sequence
    private enum ClientFlow { IDLE, CREATING_GAME, JOINING_GAME }
    private ClientFlow currentFlow = ClientFlow.IDLE;

    // Data for creating a game
    private String createUsername;
    private String createLobbyName;
    private int createNumPlayers;
    private String createVariant;

    // Data for joining a game
    private String joinUsername;
    private String joinLobbyName;

    public LobbyController(Client client) {
        this.client = client;
        root = new BorderPane();

        // Center: list of games
        gamesListView = new ListView<>();
        root.setCenter(gamesListView);

        // Bottom: text area for server messages
        serverMessagesArea = new TextArea();
        serverMessagesArea.setEditable(false);
        serverMessagesArea.setPrefRowCount(8);
        root.setBottom(serverMessagesArea);

        // Top: buttons for create/join/refresh
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        createButton = new Button("Create");
        createButton.setOnAction(e -> {
            showCreateGameDialog(); 
            // If the user actually entered a lobby name, proceed
            if (createLobbyName != null && !createLobbyName.isEmpty()) {
                currentFlow = ClientFlow.CREATING_GAME;
                client.sendToServer("create");
            }
        });

        joinButton = new Button("Join");
        joinButton.setOnAction(e -> {
            // Must select a game from the list first
            if (gamesListView.getSelectionModel().getSelectedItem() == null) {
                showErrorDialog("Please select a game to join first!");
                return;
            }
            // E.g., parse out the lobby name from the selected item:
            String selected = gamesListView.getSelectionModel().getSelectedItem();
            int spaceIndex = selected.indexOf(" ");
            String extractedLobby = (spaceIndex > 0) ? selected.substring(0, spaceIndex) : selected;
            joinLobbyName = extractedLobby.trim();

            showJoinGameDialog();
            if (joinUsername != null && !joinUsername.isEmpty()) {
                currentFlow = ClientFlow.JOINING_GAME;
                client.sendToServer("join");
            }
        });

        refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> client.sendToServer("list"));

        topBar.getChildren().addAll(createButton, joinButton, refreshButton);
        root.setTop(topBar);
    }

    public BorderPane getRoot() {
        return root;
    }

    /**
     * Handle each line from the server: update the UI or respond to prompts 
     * depending on the flow (creating/joining).
     */
    public void handleServerMessage(String line) {
        serverMessagesArea.appendText(line + "\n");

        // If the server is listing games:
        if (line.startsWith("Available Games:")) {
            games.clear();
        }
        else if (line.startsWith("- ")) {
            games.add(line.substring(2).trim());
            refreshGameList();
        }
        // If the server says the game is full => open the game window
        else if (line.contains("Game full. Lets start.")) {
            openGameGUIWindow();
        }

        // ---------------------------
        // FLOW: CREATING A NEW GAME
        // ---------------------------
        if (currentFlow == ClientFlow.CREATING_GAME) {
            if (line.contains("Please input your lobby name:")) {
                client.sendToServer(createLobbyName);
            }
            else if (line.contains("Please input number of players")) {
                client.sendToServer(String.valueOf(createNumPlayers));
            }
            else if (line.contains("Please input 'Std'")) {
                client.sendToServer(createVariant);
            }
            else if (line.contains("Please input your player name:")) {
                client.sendToServer(createUsername);
            }
            else if (line.contains("A game with lobby name")) {
                // The server says the lobby already exists
                showErrorDialog("That lobby name already exists. Try another name.");
                currentFlow = ClientFlow.IDLE;
            }
            else if (line.contains("joined their own created game")) {
                // That means the creation was successful
                currentFlow = ClientFlow.IDLE;
                // Optionally refresh the list so we see the newly created game
                client.sendToServer("list");
            }
        }

        // ---------------------------
        // FLOW: JOINING A GAME
        // ---------------------------
        else if (currentFlow == ClientFlow.JOINING_GAME) {
            if (line.contains("Please input your player name:")) {
                client.sendToServer(joinUsername);
            }
            else if (line.contains("Please input your lobby name:")) {
                client.sendToServer(joinLobbyName);
            }
            else if (line.contains("Cannot find game with lobby name")) {
                showErrorDialog("Cannot find game: " + joinLobbyName);
                currentFlow = ClientFlow.IDLE;
            }
            else if (line.contains("Game '" + joinLobbyName + "' is full")) {
                showErrorDialog("That game is full!");
                currentFlow = ClientFlow.IDLE;
            }
            else if (line.contains("joined game '" + joinLobbyName + "'")) {
                // success
                currentFlow = ClientFlow.IDLE;
                // Possibly do "list" again
            }
        }
    }

    private void refreshGameList() {
        gamesListView.getItems().setAll(games);
    }

    // --------------------------------------------------------
    // DIALOGS FOR "CREATE" AND "JOIN" (fills in class fields)
    // --------------------------------------------------------
    private void showCreateGameDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create Game");

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        Label lobbyLabel = new Label("Lobby Name:");
        TextField lobbyField = new TextField();

        Label playersLabel = new Label("Number of Players:");
        ComboBox<Integer> playersBox = new ComboBox<>();
        playersBox.getItems().addAll(2,3,4,6);
        playersBox.setValue(2);

        Label variantLabel = new Label("Variant:");
        ComboBox<String> variantBox = new ComboBox<>();
        variantBox.getItems().addAll("std","super");
        variantBox.setValue("std");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(lobbyLabel, 0, 1);
        grid.add(lobbyField, 1, 1);
        grid.add(playersLabel, 0, 2);
        grid.add(playersBox, 1, 2);
        grid.add(variantLabel, 0, 3);
        grid.add(variantBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                createUsername = userField.getText().trim();
                createLobbyName = lobbyField.getText().trim();
                createNumPlayers = playersBox.getValue();
                createVariant = variantBox.getValue();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showJoinGameDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Join Game");

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        ButtonType okButtonType = new ButtonType("Join", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                joinUsername = userField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void openGameGUIWindow() {
        // For now, just show an alert or placeholder
        Alert alert = new Alert(Alert.AlertType.INFORMATION, 
            "Game is full. Opening game window now!", ButtonType.OK);
        alert.showAndWait();
    }
}
