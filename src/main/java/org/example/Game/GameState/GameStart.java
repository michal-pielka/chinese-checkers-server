package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player.Player;

import java.util.Random;

/**
 * Represents the state of the game when it is about to start.
 */
public class GameStart implements GameState {

    /**
     * Randomly picks a starting player and transitions to GameOn.
     *
     * @param game the game instance
     */
    @Override
    public void handle(Game game) {
        // Randomly pick a starting player index
        Random random = new Random();
        int startingPlayer = random.nextInt(game.getMaxPlayers());
        game.setCurrentPlayer(startingPlayer);

        // Get that player's actual username
        String startName = game.getPlayers().get(startingPlayer).getName();
        game.broadcastMessage("It's " + startName + "'s turn first!");

        // Transition to GameOn
        game.setState(new GameOn());
        game.getState().handle(game);
    }

    /**
     * No more players can be added once the game is starting.
     *
     * @param game   the game instance
     * @param player the player to add
     */
    @Override
    public void addPlayer(Game game, Player player) {
        // No more adding players once we've started
        game.broadcastMessage("Game is full. Cannot add new players.");
    }

    /**
     * Not valid at this stage; the game will start momentarily.
     *
     * @param game the game instance
     * @param x1   the starting x-coordinate
     * @param y1   the starting y-coordinate
     * @param x2   the ending x-coordinate
     * @param y2   the ending y-coordinate
     */
    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        // Not valid at this stage
        game.broadcastMessage("Game is not started yet.");
    }
}
