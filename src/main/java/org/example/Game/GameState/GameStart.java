package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

import java.util.Random;

/**
 * Represents the state of the game when it is about to start.
 */
public class GameStart implements GameState{

    @Override
    public void handle(Game game) {
        Random random = new Random();
        int startingPlayer = random.nextInt(game.getMaxPlayers());
        game.setCurrentPlayer(startingPlayer);
        game.broadcastMessage("Player: " + (startingPlayer+1) + " " + game.getPlayers().get(startingPlayer).getName() + " goes first.");
        game.setState(new GameOn());
        game.getState().handle(game);
    }

    @Override
    public void addPlayer(Game game, Player player) {
        game.broadcastMessage("Game is full. Cannot add new players.");
    }

    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        game.broadcastMessage("Game is not started yet.");
    }
}
