package org.example.Server;

import java.io.IOException;

/**
 * Entry point for the server application.
 */
public class RunServer {
    private static final int PORT = 59899;
    private static final int THREAD_POOL_SIZE = 20; // Adjust based on expected load

    public static void main(String[] args) {
        try {
            Server server = new Server(PORT, THREAD_POOL_SIZE);
            server.start();

        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
        }
    }
}
