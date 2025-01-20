package org.example.Game.GameState;

import org.example.Game.Game;

import java.util.Random;

/**
 * Represents the state of the game when it is about to start.
 */
public class GameStart implements GameState {

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

    @Override
    public void addPlayer(Game game, org.example.Game.Player player) {
        // No more adding players once we've started
        game.broadcastMessage("Game is full. Cannot add new players.");
    }

    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        // Not valid at this stage
        game.broadcastMessage("Game is not started yet.");
    }
}
