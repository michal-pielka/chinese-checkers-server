package org.example.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Manages the socket connection, reads server lines in a background thread,
 * and allows the GUI to send commands back to the server.
 */
public class Client {

    /**
     * The socket connecting to the game server.
     */
    private Socket socket;

    /**
     * Reads lines from the server.
     */
    private Scanner inputReader;

    /**
     * Sends lines to the server.
     */
    private PrintWriter outputWriter;

    /**
     * A callback interface to handle incoming server messages.
     */
    private volatile MessageHandler messageHandler;

    /**
     * Functional interface for handling server messages.
     */
    public interface MessageHandler {
        /**
         * Invoked when a new message arrives from the server.
         *
         * @param line The message line.
         */
        void onServerMessage(String line);
    }

    /**
     * Constructs a new Client and attempts to connect to the specified host/port.
     *
     * @param host The hostname or IP address of the server.
     * @param port The port number on which the server is listening.
     * @throws IOException If the connection fails.
     */
    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputReader = new Scanner(socket.getInputStream());
        outputWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to server: " + host + ":" + port);
    }

    /**
     * Sets the MessageHandler callback for incoming messages.
     *
     * @param handler The MessageHandler to set.
     */
    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    /**
     * Sends a line of text to the server.
     *
     * @param line The text to send.
     */
    public void sendToServer(String line) {
        outputWriter.println(line);
    }

    /**
     * Starts a background thread that listens for messages from the server
     * and dispatches them to the MessageHandler if set.
     */
    public void startListening() {
        Thread t = new Thread(() -> {
            try {
                while (inputReader.hasNextLine()) {
                    String line = inputReader.nextLine();
                    if (messageHandler != null) {
                        messageHandler.onServerMessage(line);
                    }
                }
            } catch (Exception e) {
                System.err.println("Connection lost: " + e.getMessage());
            }
        }, "ServerListenerThread");
        t.setDaemon(true);
        t.start();
    }
}
