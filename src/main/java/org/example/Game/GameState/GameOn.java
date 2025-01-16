package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.GameRules.GameRules;
import org.example.Game.Player;

/**
 * Represents the state of the game when it is actively being played.
 */
public class GameOn implements GameState {

    @Override
    public void handle(Game game) {
        game.broadcastMessage("Move of player: " + (game.getCurrentPlayer()+1) + " " + game.getPlayers().get(game.getCurrentPlayer()).getName());
    }

    @Override
    public void addPlayer(Game game, Player player) {
        System.out.println("Game is on. Cannot add player.");
    }

    @Override
    public void play(Game game, int x1, int y1, int x2, int y2) {
        GameRules rules = game.getRules();
        if(rules.isMoveValid(game.getBoard(), game.getCurrentPlayer()+1, x1,y1,x2,y2)) {
            game.getBoard().movePeg(x1,y1,x2,y2);
            game.broadcastMessage("Moved from " + x1 + ":" + y1 + " to " + x2 + ":" + y2);
            if(rules.checkForWin(game.getBoard(),game.getCurrentPlayer() + 1)) {
                game.setState(new GameOver(game.getCurrentPlayer() + 1));
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
