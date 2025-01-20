package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.GameRules.GameRules;

/**
 * Represents the state of the game when it is actively being played.
 */
public class GameOn implements GameState {

    /**
     * Informs all players whose turn it is.
     *
     * @param game the game instance
     */
    @Override
    public void handle(Game game) {
        // Announce whose turn it is (by username)
        String currentPlayerName = game.getPlayers().get(game.getCurrentPlayer()).getName();
        game.broadcastMessage("It is " + currentPlayerName + "'s turn to move.");
    }

    /**
     * No players can be added once the game is ongoing.
     *
     * @param game   the game instance
     * @param player the player to add
     */
    @Override
    public void addPlayer(Game game, org.example.Game.Player player) {
        System.out.println("Game is on. Cannot add player.");
    }

    /**
     * Processes a move in the ongoing game state:
     * checks validity, performs the move, checks for a win, and ends turn.
     *
     * @param game the game instance
     * @param x1   the starting x-coordinate
     * @param y1   the starting y-coordinate
     * @param x2   the ending x-coordinate
     * @param y2   the ending y-coordinate
     */
    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        GameRules rules = game.getRules();
        int currentPlayer = game.getCurrentPlayer();
        int currentPlayerId = currentPlayer + 1; // 1-based
        String currentPlayerName = game.getPlayers().get(currentPlayer).getName();

        // Is the move valid for that player's color (1-based ID)?
        if(rules.isMoveValid(game.getBoard(), currentPlayerId, x1, y1, x2, y2)) {
            game.getBoard().movePeg(x1, y1, x2, y2);
            game.broadcastMessage("Moved from " + x1 + ":" + y1 + " to " + x2 + ":" + y2);

            // Check for a win
            if(rules.checkForWin(game.getBoard(), currentPlayerId)) {
                // Pass the 1-based ID to GameOver
                game.setState(new GameOver(currentPlayerId));
                game.getState().handle(game);
            }
            else {
                game.endTurn();
                handle(game);
            }
        }
        else {
            game.broadcastMessage("This move is not valid. Your turn ends.");
            game.endTurn();
            handle(game);
        }
    }
}
