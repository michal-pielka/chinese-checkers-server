package org.Example.Game;

import org.example.Game.Board.Board;
import org.example.Game.Game;
import org.example.Game.GameRules.GameRules;
import org.example.Game.Player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameTest {
    private Game game;
    private Player p1;
    private Player p2;
    private Board board;
    private GameRules rules;

    @BeforeEach
    void setUp() {
        board = mock(Board.class);
        rules = mock(GameRules.class);
        game = new Game("TestGame", 2, board, rules);
        p1 = mock(Player.class);
        p2 = mock(Player.class);
    }

    @Test
    void testAddPlayer() {
        game.addPlayer(p1);
        assertEquals(1, game.getPlayers().size());

        game.addPlayer(p2);
        assertEquals(2, game.getPlayers().size());

        Player p3 = mock(Player.class);
        game.addPlayer(p3);
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void testMove() {
        game.addPlayer(p1);
        game.addPlayer(p2);

        when(rules.isMoveValid(board, 1, 0,0,1,1)).thenReturn(true);

        game.setCurrentPlayer(0);
        game.move(p1, 0, 0, 1, 1);
        verify(board).movePeg(0, 0, 1, 1);
    }

    @Test
    void testInvalidMove() {
        game.addPlayer(p1);
        game.addPlayer(p2);

        when(rules.isMoveValid(board, 1, 0,0,1,1)).thenReturn(false);

        game.setCurrentPlayer(0);
        game.move(p1, 0, 0, 1, 1);
        verify(board, never()).movePeg(0, 0, 1, 1);
    }

    @Test
    void testEndTurn() {
        game.addPlayer(p1);
        game.addPlayer(p2);

        game.setCurrentPlayer(0);

        game.endTurn();
        assertEquals(1, game.getCurrentPlayer());

        game.endTurn();
        assertEquals(0, game.getCurrentPlayer());
    }


}
