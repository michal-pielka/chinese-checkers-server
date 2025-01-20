package org.example.Server.States;

import org.example.Server.UserSession;
import org.example.Server.Commands.Command;

import java.util.HashMap;

/**
 * An abstract base class representing the state of a user (e.g. in lobby, in game, etc.).
 * Subclasses implement state-specific behavior.
 */
public abstract class UserState {

    /**
     * A map of available commands for this state, keyed by command name.
     */
    protected HashMap<String, Command> commands;

    /**
     * Handles a command provided by the user.
     *
     * @param session     The UserSession object.
     * @param commandLine The raw command string entered by the user.
     */
    public abstract void handleCommand(UserSession session, String commandLine);
}

