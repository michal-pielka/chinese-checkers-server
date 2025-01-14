package org.example.Game.GameRules;

import org.example.Game.Game;

import java.util.Set;

public interface GameRules {
    boolean isMoveValid(int x1, int y1, int x2, int y2);
    boolean checkForWin();
}
