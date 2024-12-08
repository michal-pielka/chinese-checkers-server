package org.example.Game.GameState;

import org.example.Game.Game;
import org.example.Game.Player;

public interface GameState {
    void handle(Game game);
    void addPlayer(Game game, Player player);
    void play(Game game, int startPos, int endPos);
}
