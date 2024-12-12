package org.example.Server.States;

import org.example.Game.Game;
import org.example.Server.Commands.CommandRegistry;
import org.example.Server.UserThread;
import org.example.Server.Commands.Command;
import org.example.Server.Commands.MoveCommand;

import java.util.HashMap;
import java.util.Map;

public class InGameState extends UserState {
    private CommandRegistry registry;
    private Game game;

    public InGameState(Game game) {
        registry = new CommandRegistry();
        commands = new HashMap<>();
        commands.put("move", registry.getCommand("move"));
        this.game = game;
    }

    @Override
    public void handleCommand(UserThread user, String commandLine) {
        String[] tokens = commandLine.split("\\s+");
        String commandName = tokens[0].toLowerCase();
        Command command = commands.get(commandName);

        if (command != null) {
            command.execute(user, tokens);
        } else {
            user.sendMessage("Invalid command in Game. Available commands: move");
        }
    }

    public Game getGame() {
        return this.game;
    }
}
