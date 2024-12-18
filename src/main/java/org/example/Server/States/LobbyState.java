package org.example.Server.States;

import org.example.Server.UserThread;
import org.example.Server.Commands.Command;
import org.example.Server.Commands.CommandRegistry;

import java.util.HashMap;
import java.util.Map;

public class LobbyState extends UserState {
    private CommandRegistry registry;

    public LobbyState() {
        registry = new CommandRegistry();
        commands = new HashMap<>();
        // Load lobby-specific commands
        commands.put("join", registry.getCommand("join"));
        commands.put("create", registry.getCommand("create"));
        commands.put("list", registry.getCommand("list"));
        commands.put("quit", registry.getCommand("quit"));
    }

    @Override
    public void handleCommand(UserThread user, String commandLine) {
        String[] tokens = commandLine.split("\\s+");
        String commandName = tokens[0].toLowerCase();
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(user, tokens);

        } else {
            user.sendMessage("Invalid command in Lobby. Available commands: join, create, list, quit");
        }
    }
}
