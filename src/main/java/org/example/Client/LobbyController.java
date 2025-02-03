package org.example.Client;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.example.Game.Board.Board;
import org.example.Game.Board.StdBoard;
import org.example.Game.GUI.GameWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the lobby UI, displaying available games, allowing creation/joining,
 * and transitioning the user to the in-game window when appropriate.
 */
public class LobbyController {

    /**
     * Represents the phases of this client's experience:
     * LOBBY - viewing the lobby list,
     * CREATING_GAME - in the process of creating a new game,
     * JOINING_GAME - in the process of joining an existing game,
     * IN_GAME - already in a game window.
     */
    private enum ClientFlow {
        LOBBY,
        CREATING_GAME,
        JOINING_GAME,
        IN_GAME
    }

    /**
     * Tracks the current flow/state of the lobby controller.
     */
    private ClientFlow currentFlow = ClientFlow.LOBBY;

    /**
     * The Client instance to communicate with the server.
     */
    private final Client client;

    /**
     * The root container for the lobby UI.
     */
    private final BorderPane root;

    /**
     * Displays the list of available games.
     */
    private final ListView<String> gamesListView;

    /**
     * Shows messages/responses from the server.
     */
    private final TextArea serverMessagesArea;

    /**
     * Button to create a new game.
     */
    private final Button createButton;

    /**
     * Button to join a selected game.
     */
    private final Button joinButton;

    /**
     * Button to refresh the game list.
     */
    private final Button refreshButton;

    /**
     * Holds the server-provided list of game descriptions.
     */
    private final List<String> games = new ArrayList<>();

    // Data for create/join flows
    private String createUsername;
    private String createLobbyName;
    private int createNumPlayers;
    private int createNumBots;
    private String createVariant;

    private String joinUsername;
    private String joinLobbyName;

    private String myPlayerName;
    private int myPlayerId = 0;

    /**
     * The in-game window after the lobby flow is complete.
     */
    private GameWindow gameWindow;

    /**
     * Constructs a new LobbyController with references to the client.
     *
     * @param client The Client instance used to communicate with the server.
     */
    public LobbyController(Client client) {
        this.client = client;
        root = new BorderPane();

        gamesListView = new ListView<>();
        root.setCenter(gamesListView);

        serverMessagesArea = new TextArea();
        serverMessagesArea.setEditable(false);
        serverMessagesArea.setPrefRowCount(8);
        root.setBottom(serverMessagesArea);

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        createButton = new Button("Create");
        joinButton = new Button("Join");
        refreshButton = new Button("Refresh");

        createButton.setOnAction(e -> {
            showCreateGameDialog();
            if (createLobbyName != null && !createLobbyName.isEmpty()) {
                myPlayerName = createUsername;
                currentFlow = ClientFlow.CREATING_GAME;
                client.sendToServer("create");
            }
        });

        joinButton.setOnAction(e -> {
            if (gamesListView.getSelectionModel().getSelectedItem() == null) {
                showErrorDialog("Select a game first!");
                return;
            }
            String selected = gamesListView.getSelectionModel().getSelectedItem();
            int spaceIndex = selected.indexOf(" ");
            String extractedLobby = (spaceIndex > 0) ? selected.substring(0, spaceIndex) : selected;
            joinLobbyName = extractedLobby.trim();

            showJoinGameDialog();
            if (joinUsername != null && !joinUsername.isEmpty()) {
                myPlayerName = joinUsername; // store
                currentFlow = ClientFlow.JOINING_GAME;
                client.sendToServer("join");
            }
        });

        refreshButton.setOnAction(e -> client.sendToServer("list"));

        topBar.getChildren().addAll(createButton, joinButton, refreshButton);
        root.setTop(topBar);
    }

    /**
     * Retrieves the root BorderPane of this lobby UI.
     *
     * @return the root container.
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * Handles incoming messages from the server, updating the UI or transitioning state as needed.
     *
     * @param line The message from the server.
     */
    public void handleServerMessage(String line) {
        serverMessagesArea.appendText(line + "\n");

        if (currentFlow == ClientFlow.IN_GAME && gameWindow != null) {
            gameWindow.handleServerMessage(line);
            return;
        }

        if (line.startsWith("Available Games:")) {
            games.clear();
        } else if (line.startsWith("- ")) {
            games.add(line.substring(2).trim());
            refreshGameList();
        }
        // 2) If the server says "Added player number X YYY"
        else if (line.startsWith("Added player number ")) {
            String[] tokens = line.split("\\s+");
            if (tokens.length >= 5) {
                int assignedId = 0;
                try {
                    assignedId = Integer.parseInt(tokens[3]);
                } catch (NumberFormatException ignored) { }
                String assignedName = tokens[4];
                if (myPlayerName != null && myPlayerName.equals(assignedName)) {
                    myPlayerId = assignedId;
                    System.out.println("My assigned player ID = " + myPlayerId);
                }
            }
        }

        else if (line.startsWith("Game full. Lets start.")) {
            String[] parts = line.split("\\s+");
            int finalGamePlayers = 2;

            if (parts.length >= 5) {
                try {
                    finalGamePlayers = Integer.parseInt(parts[4]);
                } catch (NumberFormatException ignored) {
                }
            }

            openGameWindow(finalGamePlayers);
            currentFlow = ClientFlow.IN_GAME;
            return;
        }

        // 4) Handle create flow
        if (currentFlow == ClientFlow.CREATING_GAME) {
            if (line.contains("Please input your lobby name:")) {
                client.sendToServer(createLobbyName);
            }
            else if (line.contains("Please input number of players")) {
                client.sendToServer(String.valueOf(createNumPlayers));
            }
            else if(line.contains("Please input number of bots")){
                client.sendToServer(String.valueOf(createNumBots));
            }
            else if (line.contains("Please input 'Std'")) {
                client.sendToServer(createVariant);
            }
            else if (line.contains("Please input your player name:")) {
                client.sendToServer(createUsername);
            }
            else if (line.contains("A game with lobby name")) {
                showErrorDialog("That lobby name already exists!");
                currentFlow = ClientFlow.LOBBY;
            }
            else if (line.contains("joined their own created game")) {
                currentFlow = ClientFlow.LOBBY;
                client.sendToServer("list");
            }
        }
        // 5) Handle join flow
        else if (currentFlow == ClientFlow.JOINING_GAME) {
            if (line.contains("Please input your player name:")) {
                client.sendToServer(joinUsername);
            }
            else if (line.contains("Please input your lobby name:")) {
                client.sendToServer(joinLobbyName);
            }
            else if (line.contains("Cannot find game with lobby name")) {
                showErrorDialog("Cannot find lobby: " + joinLobbyName);
                currentFlow = ClientFlow.LOBBY;
            }
            else if (line.contains("Game '" + joinLobbyName + "' is full")) {
                showErrorDialog("That game is full!");
                currentFlow = ClientFlow.LOBBY;
            }
            else if (line.contains("joined game '" + joinLobbyName + "'")) {
                currentFlow = ClientFlow.LOBBY;
                client.sendToServer("list");
            }
        }
    }

    /**
     * Updates the gamesListView with the current list of games.
     */
    private void refreshGameList() {
        gamesListView.getItems().setAll(games);
    }

    /**
     * Shows a dialog to gather information about the new game to create,
     * storing the input in fields if the user confirms.
     */
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

        Label botsLabel = new Label("Number of Bots:");
        Spinner<Integer> botsSpinner = new Spinner<>(0, playersBox.getValue() - 1, 0);
        botsSpinner.setEditable(true);

        Label variantLabel = new Label("Variant:");
        ComboBox<String> variantBox = new ComboBox<>();
        variantBox.getItems().addAll("std","super");
        variantBox.setValue("std");

        //Updating bots number after input on players number.
        playersBox.setOnAction(e -> {
            int maxBots = playersBox.getValue() - 1;
            botsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxBots, 0));
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(userLabel, 0, 0);  grid.add(userField, 1, 0);
        grid.add(lobbyLabel, 0, 1); grid.add(lobbyField, 1, 1);
        grid.add(playersLabel, 0, 2); grid.add(playersBox, 1, 2);
        grid.add(botsLabel, 0, 3); grid.add(botsSpinner, 1, 3);
        grid.add(variantLabel, 0, 4); grid.add(variantBox, 1, 4);


        dialog.getDialogPane().setContent(grid);

        ButtonType okBtn = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == okBtn) {
                createUsername = userField.getText().trim();
                createLobbyName = lobbyField.getText().trim();
                createNumPlayers = playersBox.getValue();
                createNumBots = botsSpinner.getValue();
                createVariant = variantBox.getValue();
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Shows a dialog to gather information about joining a game,
     * storing the input in fields if the user confirms.
     */
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

        ButtonType okBtn = new ButtonType("Join", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == okBtn) {
                joinUsername = userField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Opens the in-game window once the game is determined to be full.
     *
     * @param finalGamePlayers The final number of players in the game.
     */
    private void openGameWindow(int finalGamePlayers) {
        Board board = new StdBoard(finalGamePlayers);
        int finalId = (myPlayerId > 0 ? myPlayerId : 1);

        gameWindow = new GameWindow(client, board, finalId, myPlayerName);
        gameWindow.show();

        currentFlow = ClientFlow.IN_GAME;
    }

    /**
     * Shows an error dialog with the specified message.
     *
     * @param msg The message to display.
     */
    private void showErrorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}

