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
    private Socket socket;
    private Scanner inputReader;
    private PrintWriter outputWriter;

    private volatile MessageHandler messageHandler;
    public interface MessageHandler {
        void onServerMessage(String line);
    }

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputReader = new Scanner(socket.getInputStream());
        outputWriter = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connected to server: " + host + ":" + port);
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public void sendToServer(String line) {
        outputWriter.println(line);
    }

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
