package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

public class GameOn implements GameState {

    @Override
    public void handle(Game game) {
        game.broadcastMessage("Ruch gracza: " + game.getCurrentPlayer() + " " + game.getPlayers().get(game.getCurrentPlayer()).getName());
    }

    @Override
    public void addPlayer(Game game, Player player) {
        System.out.println("Game is on. Cannot add player.");
    }

    @Override
    public void play(Game game, int startPos, int endPos) {
        System.out.println("Moved from: " + startPos + " to : " + endPos);
        game.endTurn();
        handle(game);
    }
}
