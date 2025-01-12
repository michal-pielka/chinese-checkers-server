// File path: src/main/java/org/example/Server/UserManager.java
package org.example.Server;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Manages user sessions, creation, and related functionalities.
 */
public class UserManager {
    private final Server server;

    /**
     * Constructor to initialize the UserManager with a reference to the Server.
     *
     * @param server The server instance managing games.
     */
    public UserManager(Server server) {
        this.server = server;
    }

    /**
     * Creates a new UserSession.
     *
     * @param outputWriter The PrintWriter to send messages to the client.
     * @param inputReader  The Scanner to read user input.
     * @return A new UserSession instance.
     */
    public UserSession createUserSession(PrintWriter outputWriter, Scanner inputReader) {
        return new UserSession(server, outputWriter, inputReader);
    }

    // Additional management methods can be added here if needed in the future
}
