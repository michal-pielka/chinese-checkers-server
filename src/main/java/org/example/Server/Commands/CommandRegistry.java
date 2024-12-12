package org.example.Server.Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private Map<String, Command> commands = new HashMap<>();

    public CommandRegistry() {
        // Register commands
        register("join", new JoinCommand());
        register("create", new CreateCommand());
        register("list", new ListGamesCommand());
        register("quit", new QuitCommand());
        register("move", new MoveCommand());
    }

    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
