package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

public class WaitingForPlayers implements GameState {

    @Override
    public void handle(Game game) {
        if(game.getPlayers().size() == game.getMaxPlayers()) {
            game.broadcastMessage("Game full. Lets start.");
            game.setState(new GameStart());
            game.getState().handle(game);
        }
        else {
            game.broadcastMessage("Waiting for players. Player count: " + game.getPlayers().size());
        }
    }

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

    @Override
    public void play(Game game, int startPos, int endPos) {
        game.broadcastMessage("We are still waiting for players.");
    }
}
