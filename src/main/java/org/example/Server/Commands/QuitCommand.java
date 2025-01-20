package org.example.Server.Commands;

import org.example.Server.UserSession;

/**
 * Handles the 'quit' command, allowing the user to disconnect gracefully.
 */
public class QuitCommand implements Command {

    /**
     * Executes the quit command:
     * 1. Sends a farewell message.
     * 2. Terminates the user session.
     *
     * @param session The UserSession executing this command.
     * @param args    The command arguments (not used).
     */
    @Override
    public void execute(UserSession session, String[] args) {
        System.out.println("User " + session.getPlayer().getName() + " is disconnecting.");
        session.sendMessage("Disconnecting from the server. Goodbye!");

        // Signal the session to terminate
        session.terminate();
    }
}
