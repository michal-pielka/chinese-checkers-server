package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player.Player;

/**
 * Represents the state of the game when waiting for players to join.
 */
public class WaitingForPlayers implements GameState {

    /**
     * Called whenever the state is handled; checks if the game is full
     * and transitions to the next state (GameStart) if so.
     *
     * @param game the game instance
     */
    @Override
    public void handle(Game game) {
        if(game.getPlayers().size() == game.getMaxPlayers()) {
            game.broadcastMessage("Game full. Lets start. " + game.getMaxPlayers());
            game.setState(new GameStart());
            game.getState().handle(game);
        }
        else {
            game.broadcastMessage("Waiting for players. Player count: " + game.getPlayers().size());
        }
    }

    /**
     * Adds a player to the game if it is not full.
     *
     * @param game   the game instance
     * @param player the player to add
     */
    @Override
    public void addPlayer(Game game, Player player) {
        if(game.getPlayers().size() < game.getMaxPlayers()) {
            game.getPlayers().add(player);
            game.broadcastMessage("Added player number " + game.getPlayers().size()+ " " + player.getName());
        }
        else {
            game.broadcastMessage("Game is full, cannot add player.");
        }
        handle(game);
    }

    /**
     * If a move is attempted while still waiting for players,
     * it simply notifies that the game is not ready.
     *
     * @param game the game instance
     * @param x1   the starting x-coordinate
     * @param y1   the starting y-coordinate
     * @param x2   the ending x-coordinate
     * @param y2   the ending y-coordinate
     */
    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        game.broadcastMessage("We are still waiting for players.");
    }
}
