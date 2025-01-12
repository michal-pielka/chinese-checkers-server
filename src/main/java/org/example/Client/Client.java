package org.example.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * A client program that connects to the server, sends commands, and displays server responses.
 */
public class Client {
    private Socket socket;
    private Scanner inputReader;
    private PrintWriter outputWriter;
    private Scanner userInputScanner;

    public Client(String host, int port) throws IOException {
        initializeSocket(host, port);
        initializeStreams();
    }

    /**
     * Initializes the socket connection to the server.
     */
    private void initializeSocket(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
            System.out.println("Connected to the server at " + host + ":" + port);

        } catch (IOException e) {
            System.err.println("Unable to connect to the server: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Initializes the input and output streams for communication with the server.
     */
    private void initializeStreams() throws IOException {
        try {
            inputReader = new Scanner(socket.getInputStream());
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
            userInputScanner = new Scanner(System.in);

        } catch (IOException e) {
            System.err.println("Error initializing I/O streams: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Starts the client by initiating the listener thread and handling user input.
     */
    public void start() {
        // Start a thread to listen for messages from the server
        new Thread(new ServerListener()).start();

        // Main thread handles user input and sends commands to the server
        handleUserInput();
    }

    /**
     * Continuously reads user input and sends it to the server.
     */
    private void handleUserInput() {
        try {
            while (true) {
                if (userInputScanner.hasNextLine()) {
                    String userCommand = userInputScanner.nextLine();
                    outputWriter.println(userCommand);

                } else {
                    break; // Exit if no more input
                }
            }

        } catch (Exception e) {
            System.err.println("Error handling user input: " + e.getMessage());

        } finally {
            closeConnections();
        }
    }

    /**
     * Closes all connections and streams gracefully.
     */
    private void closeConnections() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            if (userInputScanner != null) {
                userInputScanner.close();
            }

            if (inputReader != null) {
                inputReader.close();
            }

            if (outputWriter != null) {
                outputWriter.close();
            }

            System.out.println("Disconnected from the server.");

        } catch (IOException e) {
            System.err.println("Error closing connections: " + e.getMessage());
        }
    }

    /**
     * Inner class that listens for messages from the server and displays them.
     */
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (inputReader.hasNextLine()) {
                    String serverMessage = inputReader.nextLine();
                    System.out.println(serverMessage);
                }

            } catch (Exception e) {
                System.err.println("Connection to server lost: " + e.getMessage());

            } finally {
                closeConnections();
            }
        }
    }
}
