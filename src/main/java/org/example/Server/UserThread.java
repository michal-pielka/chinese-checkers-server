package org.example.Server;

import org.example.Game.Game;
import org.example.Game.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
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
    private List<Game> games;
    /**
     * Constructor to initialize client socket.
     *
     * @param socket The client socket.
     */
    public UserThread(Socket socket, ArrayList<Game> games) {
        this.socket = socket;
        this.games = Collections.synchronizedList(games);
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
        outputWriter.println("Use one of the following: join, create");
        outputWriter.flush(); // Ensure all messages are sent immediately
    }

    /**
     * Handles the 'join' command from the client.
     */
    private void handleJoin() {
        outputWriter.println("You chose to join a game.");
        outputWriter.flush();
        // TODO: Implement join logic here d
        String playerName = askForPlayerName();
        Player player = new Player(playerName, outputWriter);
        Game game = findGame();
        game.addPlayer(player);
    }

    /**
     * Handles the 'create' command from the client.
     */
    private void handleCreate() {
        outputWriter.println("You chose to create a game.");
        outputWriter.flush();
        // TODO: Implement create logic here d
        String lobbyName = askForLobbyName();
        int maxPLayers = askForNumberOfPlayers();
        Game game = new Game(lobbyName, maxPLayers);
        games.add(game);
        String playerName = askForPlayerName();
        Player player = new Player(playerName, outputWriter);
        game.addPlayer(player);
    }

    private Game findGame() {
        String lobbyName;

        while(true) {
            lobbyName = askForLobbyName();
            synchronized (games) {
                for (Game game2 : games) {
                    if (game2.getLobbyName().equals(lobbyName)) {
                        return game2;
                    }
                }
            }
            outputWriter.println("Cannot find game. Try again.");
        }
    }

    private String askForLobbyName() {
        String name = null;
        while(name == null) {
            outputWriter.println("Input your lobby name.");
            name = inputReader.nextLine().trim().toLowerCase();
        }
        return name;
    }

    private int askForNumberOfPlayers() {
        int number=0;
        String response;
        while(number!= 2 && number!=3 && number!=4 && number!=6) {
            outputWriter.println("Input number of players.");
            response = inputReader.nextLine().trim().toLowerCase();
            try{
                number = Integer.parseInt(response);
            }
            catch(NumberFormatException e) {
                outputWriter.println("Invalid number of players.");
            }
        }
        return number;
    }

    private String askForPlayerName() {
        String name = null;
        while(name == null) {
            outputWriter.println("Input your player name.");
            name = inputReader.nextLine().trim();
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

                    default:
                        outputWriter.println("Invalid command. Please enter 'join' or 'create'.");
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
