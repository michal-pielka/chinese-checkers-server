package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

/**
 * Represents a state in the game lifecycle, defining behavior for adding players, making moves, and handling state transitions.
 */
public interface GameState {

    /**
     * Handles the current state logic.
     *
     * @param game the game instance
     */
    void handle(Game game);

    /**
     * Adds a player to the game in the current state.
     *
     * @param game   the game instance
     * @param player the player to add
     */
    void addPlayer(Game game, Player player);

    /**
     * Processes a move attempt in the current state.
     *
     * @param game the game instance
     * @param x1   the starting x-coordinate
     * @param y1   the starting y-coordinate
     * @param x2   the ending x-coordinate
     * @param y2   the ending y-coordinate
     */
    void play(Game game, int x1, int y1, int x2, int y2);
}
