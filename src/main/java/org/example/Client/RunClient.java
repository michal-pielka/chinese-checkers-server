package org.example.Client;

import java.io.IOException;

/**
 * Entry point for the client application.
 */
public class RunClient {
    private static final String HOST = "localhost";
    private static final int PORT = 59899;

    public static void main(String[] args) {
        try {
            Client client = new Client(HOST, PORT);
            client.start();

        } catch (IOException e) {
            System.err.println("Failed to start the client: " + e.getMessage());
        }
    }
}
