package org.example.Server.Commands;

import org.example.Server.UserSession;

/**
 * Represents a server-side command that can be executed by a user session.
 */
public interface Command {

    /**
     * Executes the command logic.
     *
     * @param session The UserSession executing the command.
     * @param args    The command arguments, typically tokenized user input.
     */
    void execute(UserSession session, String[] args);
}

