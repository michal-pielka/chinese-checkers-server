package org.example.Server;

import org.example.Game.Game;
import org.example.Game.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Handles communication with a single client.
 */
public class UserThread implements Runnable {
    private Socket socket;
    private Scanner inputReader;
    private PrintWriter outputWriter;
    private Server server;

    /**
     * Constructor to initialize client socket and server reference.
     *
     * @param socket The client socket.
     * @param server The server instance managing games.
     */
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            initializeStreams();
        } catch (IOException e) {
            System.err.println("Error initializing I/O streams for client " + socket + ": " + e.getMessage());
            closeSocket();
        }
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
     * Sends the start menu to the client.
     */
    private void displayStartMenu() {
        outputWriter.println("Welcome! Use one of the following commands: join, create, list, quit");
        outputWriter.flush(); // Ensure all messages are sent immediately
    }

    /**
     * Handles the 'join' command from the client.
     */
    private void handleJoin() {
        outputWriter.println("You chose to join a game.");
        outputWriter.flush();

        String playerName = askForPlayerName();
        Player player = new Player(playerName, outputWriter);

        Game game = null;
        while (game == null) {
            String lobbyName = askForLobbyName();
            game = server.findGameByName(lobbyName);
            if (game == null) {
                outputWriter.println("Cannot find game with lobby name '" + lobbyName + "'. Try again.");
            }
        }

        synchronized (game) {
            if (game.getPlayers().size() >= game.getMaxPlayers()) {
                outputWriter.println("Game '" + game.getLobbyName() + "' is full. Choose another game.");
                return;
            }
            game.addPlayer(player);
            outputWriter.println("Successfully joined game '" + game.getLobbyName() + "'.");
            game.broadcastMessage(player.getName() + " has joined the game.");
        }
    }

    /**
     * Handles the 'create' command from the client.
     */
    private void handleCreate() {
        outputWriter.println("You chose to create a game.");
        outputWriter.flush();

        String lobbyName = askForLobbyName();
        if (server.findGameByName(lobbyName) != null) {
            outputWriter.println("A game with lobby name '" + lobbyName + "' already exists. Try a different name.");
            return;
        }

        int maxPlayers = askForNumberOfPlayers();
        Game game = new Game(lobbyName, maxPlayers);

        server.addGame(game);

        String playerName = askForPlayerName();
        Player player = new Player(playerName, outputWriter);
        game.addPlayer(player);

        outputWriter.println("Game '" + lobbyName + "' created successfully with " + maxPlayers + " players.");
    }

    /**
     * Handles the 'list' command to display available games.
     */
    private void handleListGames() {
        List<Game> currentGames = server.getGames();
        if (currentGames.isEmpty()) {
            outputWriter.println("No available games to join. You can create one using the 'create' command.");
        } else {
            outputWriter.println("Available Games:");
            for (Game game : currentGames) {
                outputWriter.println("- " + game.getLobbyName() + " (" + game.getPlayers().size() + "/" + game.getMaxPlayers() + " players)");
            }
        }
    }

    /**
     * Handles the 'quit' command to disconnect the client.
     */
    private void handleQuit() {
        outputWriter.println("Disconnecting from the server. Goodbye!");
        closeSocket();
    }

    private String askForLobbyName() {
        String name = null;
        while (name == null || name.isEmpty()) {
            outputWriter.println("Please input your lobby name:");
            if (inputReader.hasNextLine()) {
                name = inputReader.nextLine().trim();
                if (name.isEmpty()) {
                    outputWriter.println("Lobby name cannot be empty.");
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
            outputWriter.println("Please input number of players (2, 3, 4, or 6):");
            if (inputReader.hasNextLine()) {
                String response = inputReader.nextLine().trim();
                try {
                    number = Integer.parseInt(response);
                    if (number != 2 && number != 3 && number != 4 && number != 6) {
                        outputWriter.println("Invalid number of players. Choose 2, 3, 4, or 6.");
                    }
                } catch (NumberFormatException e) {
                    outputWriter.println("Invalid input. Please enter a numeric value.");
                }
            } else {
                outputWriter.println("No input detected. Defaulting to 2 players.");
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
            outputWriter.println("Please input your player name:");
            if (inputReader.hasNextLine()) {
                name = inputReader.nextLine().trim();
                if (name.isEmpty()) {
                    outputWriter.println("Player name cannot be empty.");
                }
            } else {
                name = "Anonymous"; // Fallback
            }
        }
        return name;
    }

    @Override
    public void run() {
        System.out.println("Connected: " + socket);
        try {
            displayStartMenu();

            while (inputReader.hasNextLine()) {
                String clientInput = inputReader.nextLine().trim().toLowerCase();
                System.out.println("Received from " + socket + ": " + clientInput);

                switch (clientInput) {
                    case "join":
                        handleJoin();
                        break;

                    case "create":
                        handleCreate();
                        break;

                    case "list":
                        handleListGames();
                        break;

                    case "quit":
                        handleQuit();
                        return; // Exit the run method to terminate the thread

                    default:
                        outputWriter.println("Invalid command. Please enter 'join', 'create', 'list', or 'quit'.");
                        break;
                }

                displayStartMenu();
            }
        } catch (Exception e) {
            System.err.println("Error communicating with client " + socket + ": " + e.getMessage());
        } finally {
            closeSocket();
            System.out.println("Closed: " + socket);
        }
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
