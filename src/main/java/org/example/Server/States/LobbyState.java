// File path: src/main/java/org/example/Server/States/LobbyState.java
package org.example.Server.States;

import org.example.Server.UserSession;
import org.example.Server.Commands.CommandRegistry;
import org.example.Server.Commands.Command;
import org.example.Server.Commands.CommandConstants;

import java.util.HashMap;

/**
 * Represents the state of a user who is in the lobby (not in a game).
 * In this state, commands such as 'join', 'create', 'list', and 'quit' are valid.
 */
public class LobbyState extends UserState {

    /**
     * A registry that maps command names to Command objects.
     */
    private CommandRegistry registry;

    /**
     * Constructs the LobbyState and initializes the lobby-specific commands.
     */
    public LobbyState() {
        this.registry = new CommandRegistry();
        this.commands = new HashMap<>();

        // Load lobby-specific commands using constants
        commands.put(CommandConstants.JOIN, registry.getCommand(CommandConstants.JOIN));
        commands.put(CommandConstants.CREATE, registry.getCommand(CommandConstants.CREATE));
        commands.put(CommandConstants.LIST, registry.getCommand(CommandConstants.LIST));
        commands.put(CommandConstants.QUIT, registry.getCommand(CommandConstants.QUIT));
    }

    /**
     * Handles a command while the user is in the lobby.
     *
     * @param session     The UserSession associated with the user.
     * @param commandLine The raw command string.
     */
    @Override
    public void handleCommand(UserSession session, String commandLine) {
        String[] tokens = commandLine.split("\\s+");
        String commandName = tokens[0].toLowerCase();
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(session, tokens);
        } else {
            session.sendMessage("Invalid command in Lobby. Available commands: join, create, list, quit");
        }
    }
}

