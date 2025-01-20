package org.Example.Game.GameRules;
import org.example.Game.Board.StdBoard;
import org.example.Game.Board.Node;
import org.example.Game.GameRules.SuperRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SuperRulesTest {
    private StdBoard board;
    private SuperRules rules;

    @BeforeEach
    void setUp() {
        board = new StdBoard(2);
        rules = new SuperRules();
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
    void testIsMoveValid_InvalidMove() {
        String startKey = board.getTargetBase(1).get(0);
        Node startNode = board.getNode(startKey);
        startNode.setPlayer(1);

        String invalidKey = "20:20";
        boolean result = rules.isMoveValid(board, 1, startNode.getX(), startNode.getY(), 20, 20);

        assertFalse(result, "A move to an invalid position should not be valid.");
    }

    @Test
    void testIsMoveValid_JumpMove() {
        Node startNode = board.getNode("8:8");
        startNode.setPlayer(1);

        Node neighbour = startNode.neighbours.get(0);
        int dx = neighbour.getX()-startNode.getX();
        int dy = neighbour.getY()-startNode.getY();

        int midX = startNode.getX() + 2*dx;
        int midY = startNode.getY() + 2*dy;
        Node midNode = board.getNode(midX+":"+midY);
        midNode.setPlayer(2);

        int jumpX = midX + 2*dx;
        int jumpY = midY + 2*dy;
        Node jumpNode = board.getNode(jumpX+":"+jumpY);
        jumpNode.setPlayer(0);

        int clearX = startNode.getX()+dx;
        int clearY = startNode.getY()+dy;
        while(clearX != jumpX || clearY != jumpY) {
            if(clearX != midX || clearY != midY) {
                board.getNode(clearX+":"+clearY).setPlayer(0);
            }
            clearX += dx;
            clearY += dy;
        }

        boolean result = rules.isMoveValid(board, 1, 8, 8, jumpX, jumpY);
        assertTrue(result, "A jump over another player's peg to an empty node should be valid.");
    }

    @Test
    void testFindJumps() {
        Node startNode = board.getNode("8:8");
        startNode.setPlayer(1);

        Node neighbour = startNode.neighbours.get(0);
        int dx = neighbour.getX()-startNode.getX();
        int dy = neighbour.getY()-startNode.getY();

        int midX = startNode.getX() + 2*dx;
        int midY = startNode.getY() + 2*dy;
        Node midNode = board.getNode(midX+":"+midY);
        midNode.setPlayer(2);

        int jumpX = midX + 2*dx;
        int jumpY = midY + 2*dy;
        Node jumpNode = board.getNode(jumpX+":"+jumpY);
        jumpNode.setPlayer(0);

        int clearX = startNode.getX()+dx;
        int clearY = startNode.getY()+dy;
        while(clearX != jumpX || clearY != jumpY) {
            if(clearX != midX || clearY != midY) {
                board.getNode(clearX+":"+clearY).setPlayer(0);
            }
            clearX += dx;
            clearY += dy;
        }

        Set<String> jumps = rules.findJumps(board, startNode.getX(), startNode.getY(), new HashSet<>());
        assertTrue(jumps.contains(jumpX+":"+jumpY), "The jump destination should be included in the set of possible jumps.");
    }

    @Test
    void testLineClear() {
        Node startNode = board.getNode("8:8");
        startNode.setPlayer(1);

        Node neighbour = startNode.neighbours.get(0);
        int dx = neighbour.getX()-startNode.getX();
        int dy = neighbour.getY()-startNode.getY();

        int X = startNode.getX();
        int Y = startNode.getY();
        for(int i=1; i<=3; i++) {
            X += dx;
            Y += dy;
            board.getNode(X+":"+Y).setPlayer(0);
        }
        X += dx;
        Y +=dy;
        board.getNode(X+":"+Y).setPlayer(2);

        assertTrue(rules.lineClear(board, dx, dy, startNode.getX(), startNode.getY(), X, Y), "There should be no players between this two nodes.");
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
