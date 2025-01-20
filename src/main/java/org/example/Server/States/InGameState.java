// File path: src/main/java/org/example/Server/States/InGameState.java
package org.example.Server.States;

import org.example.Server.UserSession;
import org.example.Game.Game;
import org.example.Server.Commands.CommandRegistry;
import org.example.Server.Commands.Command;
import org.example.Server.Commands.CommandConstants;

import java.util.HashMap;

/**
 * Represents the state of a user who is currently in a game.
 * In this state, the user can use in-game commands (like "move").
 */
public class InGameState extends UserState {

    /**
     * The Game instance the user is playing in.
     */
    private Game game;

    /**
     * A registry of all possible commands.
     */
    private CommandRegistry registry;

    /**
     * Constructs an InGameState for a given game.
     *
     * @param game The game instance the user is part of.
     */
    public InGameState(Game game) {
        this.game = game;
        this.registry = new CommandRegistry();
        this.commands = new HashMap<>();

        commands.put(CommandConstants.MOVE, registry.getCommand(CommandConstants.MOVE));
    }

    /**
     * Handles a command while the user is in-game.
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
            session.sendMessage("Invalid command in Game. Available commands: move");
        }
    }

    /**
     * Retrieves the game instance associated with this state.
     *
     * @return The active Game object.
     */
    public Game getGame() {
        return this.game;
    }
}

