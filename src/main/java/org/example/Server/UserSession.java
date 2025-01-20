package org.example.Server;

import org.example.Game.Game;
import org.example.Game.GameRules.GameRules;
import org.example.Game.GameRules.StdRules;
import org.example.Game.GameRules.SuperRules;
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

    /**
     * Reference to the main server instance for game lookups and creation.
     */
    private final Server server;

    /**
     * A PrintWriter for sending messages back to this user/client.
     */
    private final PrintWriter outputWriter;

    /**
     * A Scanner for reading user commands from the client.
     */
    private final Scanner inputReader;

    /**
     * The Player object associated with this session.
     */
    private final Player player;

    /**
     * The user's current state (e.g., Lobby, InGame, etc.).
     */
    private UserState state;

    /**
     * Flag to indicate whether the session is active.
     */
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
     *
     * @return The Player object for this session.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the Server reference for game lookups, game creation, etc.
     *
     * @return The Server instance associated with this session.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the current state of the user.
     *
     * @return The current UserState instance.
     */
    public UserState getState() {
        return this.state;
    }

    // ----------------------------------------------------------------------
    // Methods to facilitate user interactions (called by Command classes)
    // ----------------------------------------------------------------------

    /**
     * Repeatedly prompts the user to enter a lobby name until a non-empty
     * name is provided. Returns the chosen lobby name.
     *
     * @return A non-empty lobby name.
     */
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
     * Repeatedly prompts the user to choose a game variant ("std" or "super").
     * Returns the corresponding GameRules object.
     *
     * @return A GameRules instance (StdRules or SuperRules).
     */
    public GameRules askForGameVariant() {
        String gameVariant = null;
        while (gameVariant == null || (!gameVariant.equals("std") && !gameVariant.equals("super"))) {
            sendMessage("Please input 'Std' for standard game variant or 'Super' for super game variant.");
            if(inputReader.hasNextLine()) {
                gameVariant = inputReader.nextLine().trim().toLowerCase();
            }
            else {
                gameVariant = "std";
                System.out.println("No game variant detected. Defaulting to Standard Game Variant.");
            }
        }
        if(gameVariant.equals("super")) {
            return new SuperRules();
        }
        return new StdRules();
    }

    /**
     * Sends the initial welcome/help menu to the client.
     */
    public void displayStartMenu() {
        sendMessage("Welcome! Use one of the following commands: join, create, list, quit");
    }

    /**
     * Repeatedly prompts for the number of players until a valid number (2, 3, 4, or 6) is provided.
     *
     * @return The chosen number of players.
     */
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

    /**
     * Repeatedly prompts for a player name until a non-empty name is provided.
     *
     * @return The chosen player name.
     */
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

