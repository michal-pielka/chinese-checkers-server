package org.example.Server;

import org.example.Game.Game;
import org.example.Game.Player;
import org.example.Server.States.UserState;
import org.example.Server.States.LobbyState;
import org.example.Server.States.InGameState;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Encapsulates the game-related logic, state transitions, 
 * and user interactions (prompts) for a single connected user.
 */
public class UserSession {
    private final Server server;
    private final PrintWriter outputWriter;
    private final Scanner inputReader;
    private final Player player;

    // The user's current state (Lobby, InGame, etc.)
    private UserState state;

    // Flag to indicate if the session is active
    private volatile boolean active;

    /**
     * Constructor
     *
     * @param server       The server instance for game lookups, etc.
     * @param outputWriter A PrintWriter for sending messages to the client.
     * @param inputReader  A Scanner for reading user input.
     */
    public UserSession(Server server, PrintWriter outputWriter, Scanner inputReader) {
        this.server = server;
        this.outputWriter = outputWriter;
        this.inputReader = inputReader;
        this.state = new LobbyState(); // Initial state
        this.player = new Player("defaultName", outputWriter);
        this.active = true; // Session starts as active
    }

    /**
     * Handles the command from the client, delegating to the current state.
     *
     * @param clientInput The raw command string.
     */
    public void handleCommand(String clientInput) {
        System.out.println("Handling command: " + clientInput);
        state.handleCommand(this, clientInput);
    }

    /**
     * Sends a message to the client's console.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        outputWriter.println(message);
        System.out.println("Sent message to " + player.getName() + ": " + message);
    }

    /**
     * Updates the user's state.
     *
     * @param newState The new state to set.
     */
    public void setState(UserState newState) {
        System.out.println("Transitioning user " + player.getName() + " from " 
                           + state.getClass().getSimpleName() + " to " 
                           + newState.getClass().getSimpleName());
        this.state = newState;
    }

    /**
     * Gets the currently logged-in Player instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the Server reference for game lookups, game creation, etc.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the current state of the user.
     */
    public UserState getState() {
        return this.state;
    }

    // ----------------------------------------------------------------------
    // Methods to facilitate user interactions (now called by Command classes)
    // ----------------------------------------------------------------------

    public String askForLobbyName() {
        String lobbyName = null;
        while (lobbyName == null || lobbyName.isEmpty()) {
            sendMessage("Please input your lobby name:");
            if (inputReader.hasNextLine()) {
                lobbyName = inputReader.nextLine().trim();
                if (lobbyName.isEmpty()) {
                    sendMessage("Lobby name cannot be empty.");
                    System.out.println("User " + player.getName() + " entered an empty lobby name.");
                }
            } else {
                lobbyName = "default_lobby";
                System.out.println("No lobby name input detected. Defaulting to 'default_lobby'.");
            }
        }
        return lobbyName;
    }

    /**
     * Sends the initial welcome/help menu to the client.
     */
    public void displayStartMenu() {
        sendMessage("Welcome! Use one of the following commands: join, create, list, quit");
    }

    public int askForNumberOfPlayers() {
        int number = 0;
        while (number != 2 && number != 3 && number != 4 && number != 6) {
            sendMessage("Please input number of players (2, 3, 4, or 6):");
            if (inputReader.hasNextLine()) {
                String response = inputReader.nextLine().trim();
                try {
                    number = Integer.parseInt(response);
                    if (number != 2 && number != 3 && number != 4 && number != 6) {
                        sendMessage("Invalid number of players. Choose 2, 3, 4, or 6.");
                        System.out.println("User " + player.getName() + " entered invalid number of players: " + number);
                    }
                } catch (NumberFormatException e) {
                    sendMessage("Invalid input. Please enter a numeric value.");
                    System.out.println("User " + player.getName() + " entered non-numeric input for number of players.");
                }
            } else {
                // Fallback
                sendMessage("No input detected. Defaulting to 2 players.");
                number = 2;
                System.out.println("No input detected for number of players. Defaulting to 2.");
            }
        }
        return number;
    }

    public String askForPlayerName() {
        String playerName = null;
        while (playerName == null || playerName.isEmpty()) {
            sendMessage("Please input your player name:");
            if (inputReader.hasNextLine()) {
                playerName = inputReader.nextLine().trim();
                if (playerName.isEmpty()) {
                    sendMessage("Player name cannot be empty.");
                    System.out.println("User " + player.getName() + " entered an empty player name.");
                }
            } else {
                playerName = "Anonymous";
                System.out.println("No player name input detected. Defaulting to 'Anonymous'.");
            }
        }
        return playerName;
    }

    /**
     * Signals the session to terminate.
     */
    public void terminate() {
        this.active = false;
        System.out.println("Session for user " + player.getName() + " is marked for termination.");
    }

    /**
     * Checks if the session is active.
     *
     * @return True if active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }
}