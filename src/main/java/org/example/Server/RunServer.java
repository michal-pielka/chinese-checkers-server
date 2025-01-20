package org.example.Server;

import java.io.IOException;

/**
 * Entry point for the server application.
 */
public class RunServer {

    /**
     * Default server port.
     */
    private static final int PORT = 59899;

    /**
     * Default thread pool size (tunable based on load).
     */
    private static final int THREAD_POOL_SIZE = 20; 

    /**
     * Main method to start the server.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        try {
            Server server = new Server(PORT, THREAD_POOL_SIZE);
            server.start();

        } catch (IOException e) {
            System.err.println("Failed to start the server: " + e.getMessage());
        }
    }
}

