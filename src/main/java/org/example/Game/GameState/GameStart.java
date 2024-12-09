package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

import java.util.Random;

public class GameStart implements GameState{

    @Override
    public void handle(Game game) {
        Random random = new Random();
        int startingPlayer = random.nextInt(game.getMaxPlayers());
        game.setCurrentPlayer(startingPlayer);
        game.broadcastMessage("Rozpoczyna gracz: " + startingPlayer + " " + game.getPlayers().get(startingPlayer).getName());
        game.setState(new GameOn());
    }

    @Override
    public void addPlayer(Game game, Player player) {
        game.broadcastMessage("Game is full. Cannot add new players.");
    }

    @Override
    public void play(Game game, int startPos, int endPos) {
        game.broadcastMessage("Game is not started yet.");
    }
}
