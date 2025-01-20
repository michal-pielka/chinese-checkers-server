package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

/**
 * Represents the state of the game when it has ended.
 */
public class GameOver implements GameState {
    int winner;

    /**
     * Constructs a new GameOver state with the specified winner.
     *
     * @param winner the index of the winning player
     */
    public GameOver(int winner) {
        this.winner = winner;
    }

    @Override
    public void handle(Game game) {
        game.broadcastMessage("PLAYER " + winner + ": " + game.getCurrentPlayer()+1 +" WON!!");
    }

    @Override
    public void addPlayer(Game game, Player player) {
        game.broadcastMessage("Cannot add players the game is over.");
    }

    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        game.broadcastMessage("Cannot move the game is over.");
    }
}
