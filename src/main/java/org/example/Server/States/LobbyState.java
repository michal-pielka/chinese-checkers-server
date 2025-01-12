// File path: src/main/java/org/example/Server/States/LobbyState.java
package org.example.Server.States;

import org.example.Server.UserSession;
import org.example.Server.Commands.CommandRegistry;
import org.example.Server.Commands.Command;
import org.example.Server.Commands.CommandConstants;

import java.util.HashMap;

public class LobbyState extends UserState {
    private CommandRegistry registry;

    public LobbyState() {
        this.registry = new CommandRegistry();
        this.commands = new HashMap<>();

        // Load lobby-specific commands using constants
        commands.put(CommandConstants.JOIN, registry.getCommand(CommandConstants.JOIN));
        commands.put(CommandConstants.CREATE, registry.getCommand(CommandConstants.CREATE));
        commands.put(CommandConstants.LIST, registry.getCommand(CommandConstants.LIST));
        commands.put(CommandConstants.QUIT, registry.getCommand(CommandConstants.QUIT));
    }

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
