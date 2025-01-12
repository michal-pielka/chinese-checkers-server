package org.example.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Handles communication with a single client.
 */
public class UserThread implements Runnable {
    private final Socket socket;
    private final Server server;
    private Scanner inputReader;
    private PrintWriter outputWriter;

    private UserSession userSession;
    private final UserManager userManager;

    /**
     * Constructor to initialize client socket and server reference.
     *
     * @param socket The client socket.
     * @param server The server instance managing games.
     */
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.userManager = new UserManager(server);

        try {
            initializeStreams();
            this.userSession = userManager.createUserSession(outputWriter, inputReader);

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
        inputReader = new Scanner(socket.getInputStream());
        outputWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Entry point for the user thread.
     */
    @Override
    public void run() {
        System.out.println("Connected: " + socket);

        try {
            userSession.displayStartMenu();

            while (inputReader.hasNextLine()) {
                String clientInput = inputReader.nextLine().trim();
                System.out.println("Received from " + socket + ": " + clientInput);

                userSession.handleCommand(clientInput);
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
