package org.example.Server;

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

    /**
     * Constructor to initialize client socket.
     *
     * @param socket The client socket.
     */
    public UserThread(Socket socket) {
        this.socket = socket;
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
    }

    /**
     * Handles the 'create' command from the client.
     */
    private void handleCreate() {
        outputWriter.println("You chose to create a game.");
        outputWriter.flush();
        // TODO: Implement create logic here d
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
