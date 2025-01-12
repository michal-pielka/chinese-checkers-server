package org.example.Server.Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private Map<String, Command> commands = new HashMap<>();

    public CommandRegistry() {
        register(CommandConstants.JOIN, new JoinCommand());
        register(CommandConstants.CREATE, new CreateCommand());
        register(CommandConstants.LIST, new ListGamesCommand());
        register(CommandConstants.QUIT, new QuitCommand());
        register(CommandConstants.MOVE, new MoveCommand());
    }

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
