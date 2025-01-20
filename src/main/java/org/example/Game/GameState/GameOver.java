package org.example.Game.GameState;

import org.example.Game.Game;

/**
 * Represents the state of the game when it has ended.
 */
public class GameOver implements GameState {

    /**
     * A 1-based index of the winning player.
     */
    private final int winner;

    /**
     * Constructs a new GameOver state with the specified winner (1-based).
     *
     * @param winner the 1-based index of the winning player
     */
    public GameOver(int winner) {
        this.winner = winner;
    }

    /**
     * Announces the winner to all players.
     *
     * @param game the game instance
     */
    @Override
    public void handle(Game game) {
        // Convert winner (1-based) to array index
        int winnerIndex = winner - 1;
        String winnerName = game.getPlayers().get(winnerIndex).getName();

        // Announce the actual username as the winner
        game.broadcastMessage(winnerName + " WON!!");
    }

    /**
     * No new players can join once the game is over.
     *
     * @param game   the game instance
     * @param player the player to add
     */
    @Override
    public void addPlayer(Game game, org.example.Game.Player player) {
        game.broadcastMessage("Cannot add players; the game is over.");
    }

    /**
     * No moves are allowed once the game is over.
     *
     * @param game the game instance
     * @param x1   the starting x-coordinate
     * @param y1   the starting y-coordinate
     * @param x2   the ending x-coordinate
     * @param y2   the ending y-coordinate
     */
    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        game.broadcastMessage("Cannot move; the game is over.");
    }
}
