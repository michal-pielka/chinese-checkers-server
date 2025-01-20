package org.example.Server.Commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a registry of all available command objects, keyed by their string name.
 * Other classes can retrieve commands from this registry.
 */
public class CommandRegistry {

    /**
     * Internal storage of commands, keyed by their lowercase name.
     */
    private Map<String, Command> commands = new HashMap<>();

    /**
     * Creates a new CommandRegistry and registers all known commands.
     */
    public CommandRegistry() {
        register(CommandConstants.JOIN, new JoinCommand());
        register(CommandConstants.CREATE, new CreateCommand());
        register(CommandConstants.LIST, new ListGamesCommand());
        register(CommandConstants.QUIT, new QuitCommand());
        register(CommandConstants.MOVE, new MoveCommand());
    }

    /**
     * Registers a command with a given name in the registry.
     *
     * @param commandName The name (string) of the command.
     * @param command     The Command instance to register.
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Retrieves the command associated with the given command name.
     *
     * @param commandName The name of the command.
     * @return The Command instance, or null if not found.
     */
    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
