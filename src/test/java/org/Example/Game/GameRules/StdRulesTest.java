package org.Example.Game.GameRules;
import org.example.Game.Board.StdBoard;
import org.example.Game.Board.Node;
import org.example.Game.GameRules.StdRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class StdRulesTest {
    private StdBoard board;
    private StdRules rules;

    @BeforeEach
    void setUp() {
        board = new StdBoard(2);
        rules = new StdRules();
    }

    @Test
    void testIsMoveValid_DirectMove() {
        Node startNode = board.getNode("7:7");
        startNode.setPlayer(1);

        Node neighbour = startNode.neighbours.get(0);
        neighbour.setPlayer(0);

        boolean result = rules.isMoveValid(board, 1, 7,7, neighbour.getX(), neighbour.getY());
        assertTrue(result, "A direct move to a neighbouring empty node should be valid.");
    }

    @Test
    void testIsMoveValid_JumpMove() {
        Node startNode = board.getNode("8:8");
        startNode.setPlayer(1);

        Node neighbour = startNode.neighbours.get(0);
        neighbour.setPlayer(2);

        int jumpX = startNode.getX() + 2*(neighbour.getX()-startNode.getX());
        int jumpY = startNode.getY() + 2*(neighbour.getY()-startNode.getY());

        Node jumpNode = board.getNode(jumpX+":"+jumpY);
        jumpNode.setPlayer(0);

        boolean result = rules.isMoveValid(board, 1, 8, 8, jumpX, jumpY);
        assertTrue(result, "A jump over another player's peg to an empty node should be valid.");
    }

    @Test
    void testIsMoveValid_InvalidMove() {
        String startKey = board.getTargetBase(1).get(0);
        Node startNode = board.getNode(startKey);
        startNode.setPlayer(1);

        String invalidKey = "20:20";
        boolean result = rules.isMoveValid(board, 1, startNode.getX(), startNode.getY(), 20, 20);

        assertFalse(result, "A move to an invalid position should not be valid.");
    }

    @Test
    void testFindJumps() {
        String startKey = "8:8";
        Node startNode = board.getNode(startKey);
        startNode.setPlayer(1);

        Node middleNode = startNode.neighbours.get(0);
        middleNode.setPlayer(2);

        int jumpX = 2 * (middleNode.getX() - startNode.getX()) + startNode.getX();
        int jumpY = 2 * (middleNode.getY() - startNode.getY()) + startNode.getY();;

        Set<String> jumps = rules.findJumps(board, startNode.getX(), startNode.getY(), new java.util.HashSet<>());

        assertTrue(jumps.contains(jumpX+":"+jumpY), "The jump destination should be included in the set of possible jumps.");
    }

    @Test
    void testCheckForWin_NoWin() {
        String targetKey = board.getTargetBase(1).get(0);
        Node targetNode = board.getNode(targetKey);
        targetNode.setPlayer(2);

        boolean result = rules.checkForWin(board, 1);

        assertFalse(result, "The player should not win if not all target base nodes are occupied.");
    }

    @Test
    void testCheckForWin_Win() {
        for (String key : board.getTargetBase(1)) {
            Node node = board.getNode(key);
            node.setPlayer(1);
        }

        boolean result = rules.checkForWin(board, 1);

        assertTrue(result, "The player should win if all target base nodes are occupied.");
    }
}
