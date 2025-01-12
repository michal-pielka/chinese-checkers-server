// File path: src/main/java/org/example/Server/States/InGameState.java
package org.example.Server.States;

import org.example.Server.UserSession;
import org.example.Game.Game;
import org.example.Server.Commands.CommandRegistry;
import org.example.Server.Commands.Command;
import org.example.Server.Commands.CommandConstants;

import java.util.HashMap;

public class InGameState extends UserState {
    private Game game;
    private CommandRegistry registry;

    public InGameState(Game game) {
        this.game = game;
        this.registry = new CommandRegistry();
        this.commands = new HashMap<>();

        commands.put(CommandConstants.MOVE, registry.getCommand(CommandConstants.MOVE));
    }

    @Override
    public void handleCommand(UserSession session, String commandLine) {
        String[] tokens = commandLine.split("\\s+");
        String commandName = tokens[0].toLowerCase();
        Command command = commands.get(commandName);

        if (command != null) {
            command.execute(session, tokens);

        } else {
            session.sendMessage("Invalid command in Game. Available commands: move");
        }
    }

    public Game getGame() {
        return this.game;
    }
}
