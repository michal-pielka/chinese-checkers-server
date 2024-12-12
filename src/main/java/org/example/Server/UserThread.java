package org.example.Server;

import org.example.Game.Game;
import org.example.Game.Player;
import org.example.Server.States.InGameState;
import org.example.Server.States.LobbyState;
import org.example.Server.States.UserState;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Handles communication with a single client.
 */
public class UserThread implements Runnable {
    private Socket socket;
    private Scanner inputReader;
    private PrintWriter outputWriter;
    private Server server;
    private UserState state;
    private Player player;

    /**
     * Constructor to initialize client socket and server reference.
     *
     * @param socket The client socket.
     * @param server The server instance managing games.
     */
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.state = new LobbyState(); // Initial state

        try {
            initializeStreams();

        } catch (IOException e) {
            System.err.println("Error initializing I/O streams for client " + socket + ": " + e.getMessage());
            closeSocket();
        }
        this.player = new Player("defaultName", outputWriter);
    }

    /**
     * Initializes the input and output streams for communication with the client.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void initializeStreams() throws IOException {
        try {
            inputReader = new Scanner(socket.getInputStream());
            outputWriter = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            System.err.println("Error initializing streams for client " + socket + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Sends messages to the client.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        outputWriter.println(message);
    }

    /**
     * Sets the user's state.
     *
     * @param newState The new state to set.
     */
    public void setState(UserState newState) {
        this.state = newState;
    }

    /**
     * Entry point for the user thread.
     */
    @Override
    public void run() {
        System.out.println("Connected: " + socket);
        try {
            displayStartMenu();

            while (inputReader.hasNextLine()) {
                String clientInput = inputReader.nextLine().trim();
                System.out.println("Received from " + socket + ": " + clientInput);

                state.handleCommand(this, clientInput);
            }

        } catch (Exception e) {
            System.err.println("Error communicating with client " + socket + ": " + e.getMessage());

        } finally {
            closeSocket();
            System.out.println("Closed: " + socket);
        }
    }

    /**
     * Sends the start menu to the client.
     */
    private void displayStartMenu() {
        sendMessage("Welcome! Use one of the following commands: join, create, list, quit");
    }

    // Command Handlers

    public void handleJoin() {
        sendMessage("You chose to join a game.");

        String playerName = askForPlayerName();
        player.setName(playerName);

        Game game = null;
        while (game == null) {
            String lobbyName = askForLobbyName();
            game = server.findGameByName(lobbyName);
            if (game == null) {
                sendMessage("Cannot find game with lobby name '" + lobbyName + "'. Try again.");
            }
        }

        synchronized (game) {
            if (game.getPlayers().size() >= game.getMaxPlayers()) {
                sendMessage("Game '" + game.getLobbyName() + "' is full. Choose another game.");
                return;
            }

            game.addPlayer(player);
            //sendMessage("Successfully joined game '" + game.getLobbyName() + "'.");
            game.broadcastMessage(player.getName() + " has joined the game.");

            // Transition to InGameState
            InGameState inGameState = new InGameState(game);
            setState(inGameState);
        }
    }

    public void handleCreate() {
        sendMessage("You chose to create a game.");

        String lobbyName = askForLobbyName();
        if (server.findGameByName(lobbyName) != null) {
            sendMessage("A game with lobby name '" + lobbyName + "' already exists. Try a different name.");
            return;
        }

        int maxPlayers = askForNumberOfPlayers();
        Game game = new Game(lobbyName, maxPlayers);

        server.addGame(game);

        String playerName = askForPlayerName();
        player.setName(playerName);
        game.addPlayer(player);

        //sendMessage("Game '" + lobbyName + "' created successfully with " + maxPlayers + " players.");

        // Transition to InGameState
        InGameState inGameState = new InGameState(game);
        setState(inGameState);
    }

    public void handleListGames() {
        var currentGames = server.getGames();
        if (currentGames.isEmpty()) {
            sendMessage("No available games to join. You can create one using the 'create' command.");
        } else {
            sendMessage("Available Games:");
            for (Game game : currentGames) {
                sendMessage("- " + game.getLobbyName() + " (" + game.getPlayers().size() + "/" + game.getMaxPlayers() + " players)");
            }
        }
    }

    public void handleMove(int startPos, int endPos) {
        if(state instanceof InGameState) {
            InGameState inGameState = (InGameState) state;
            Game game = inGameState.getGame();
            game.move(player, startPos, endPos);
        }
    }

    public void handleQuit() {
        sendMessage("Disconnecting from the server. Goodbye!");
        closeSocket();
    }

    // Helper Methods
    private String askForLobbyName() {
        String name = null;
        while (name == null || name.isEmpty()) {
            sendMessage("Please input your lobby name:");
            if (inputReader.hasNextLine()) {
                name = inputReader.nextLine().trim();
                if (name.isEmpty()) {
                    sendMessage("Lobby name cannot be empty.");
                }
            } else {
                name = "default_lobby"; // Fallback
            }
        }
        return name;
    }

    /**
     * Asks the client for the number of players.
     *
     * @return The valid number of players entered by the client.
     */
    private int askForNumberOfPlayers() {
        int number = 0;
        while (number != 2 && number != 3 && number != 4 && number != 6) {
            sendMessage("Please input number of players (2, 3, 4, or 6):");
            if (inputReader.hasNextLine()) {
                String response = inputReader.nextLine().trim();
                try {
                    number = Integer.parseInt(response);
                    if (number != 2 && number != 3 && number != 4 && number != 6) {
                        sendMessage("Invalid number of players. Choose 2, 3, 4, or 6.");
                    }
                } catch (NumberFormatException e) {
                    sendMessage("Invalid input. Please enter a numeric value.");
                }
            } else {
                sendMessage("No input detected. Defaulting to 2 players.");
                number = 2;
            }
        }
        return number;
    }

    /**
     * Asks the client for their player name.
     *
     * @return The player name entered by the client.
     */
    private String askForPlayerName() {
        String name = null;
        while (name == null || name.isEmpty()) {
            sendMessage("Please input your player name:");
            if (inputReader.hasNextLine()) {
                name = inputReader.nextLine().trim();
                if (name.isEmpty()) {
                    sendMessage("Player name cannot be empty.");
                }
            } else {
                name = "Anonymous"; // Fallback
            }
        }
        return name;
    }

    /**
     * Closes the client socket gracefully.
     */
    private void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket " + socket + ": " + e.getMessage());
        }
    }
}
