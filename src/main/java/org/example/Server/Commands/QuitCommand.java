package org.example.Server.Commands;

import org.example.Server.UserSession;

/**
 * Handles the 'quit' command, allowing the user to disconnect gracefully.
 */
public class QuitCommand implements Command {
    @Override
    public void execute(UserSession session, String[] args) {
        System.out.println("User " + session.getPlayer().getName() + " is disconnecting.");
        session.sendMessage("Disconnecting from the server. Goodbye!");

        // Signal the session to terminate
        session.terminate();
    }
}
