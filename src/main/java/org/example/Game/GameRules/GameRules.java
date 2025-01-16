package org.example.Game.GameRules;

import org.example.Game.Board.Board;

/**
 * Defines the rules for the game.
 * Implementations of this interface determine the validity of moves and check for winning conditions.
 */
public interface GameRules {

    /**
     * Validates whether a move is legal according to the game rules.
     *
     * @param board  the game board on which the move is being made.
     * @param player the player attempting the move.
     * @param x1     the x-coordinate of the starting position.
     * @param y1     the y-coordinate of the starting position.
     * @param x2     the x-coordinate of the destination position.
     * @param y2     the y-coordinate of the destination position.
     * @return true if the move is valid, false otherwise.
     */
    boolean isMoveValid(Board board, int player, int x1, int y1, int x2, int y2);

    /**
     * Checks if the specified player has met the winning conditions.
     *
     * @param board  the game board.
     * @param player the player to check for a win.
     * @return true if the player has won, false otherwise.
     */
    boolean checkForWin(Board board, int player);
}
