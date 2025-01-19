package org.Example.Game.Board;
import org.example.Game.Board.Node;
import org.example.Game.Board.StdBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StdBoardTest {
    private StdBoard board;

    @BeforeEach
    public void setUp() {
        board = new StdBoard(4);
    }

    @Test
    void testBoardInitialization() {
        Map<String, Node> nodes = board.getNodes();
        assertFalse(nodes.isEmpty(), "The board should contain nodes.");

        List<String> p1TargetBase = board.getTargetBase(1);
        assertNotNull(p1TargetBase, "Player 1's base should be initialized.");
        assertEquals(10, p1TargetBase.size(), "Player 1's base should contain 10 nodes.");
    }

    @Test
    void testMovePeg() {
        String startKey = "4:3";
        Node startNode = board.getNode(startKey);

        String endKey = "5:5";
        Node endNode = board.getNode(endKey);

        startNode.setPlayer(1);
        endNode.setPlayer(0);

        board.movePeg(4, 3, 5, 5);
        assertEquals(1, endNode.getPlayer(), "The peg should be moved to the target node.");
        assertEquals(0, startNode.getPlayer(), "The starting node should be empty.");
    }

    @Test
    void testContainsNode() {
        assertTrue(board.containsNode("4:0"), "The board should contain node 4:0.");
        assertFalse(board.containsNode("20:20"), "The board should not contain node 20:20.");
    }

    @Test
    void testGetNode() {
        Node node = board.getNode("4:0");
        assertNotNull(node, "Node 4:0 should exist on the board.");
        assertNull(board.getNode("20:20"), "Node 20:20 should not exist on the board.");
    }

    @Test
    void testGetTargetBase() {
        List<String> targetBase = board.getTargetBase(1);
        assertNotNull(targetBase, "Player 1's target base should exist.");
        assertEquals(10, targetBase.size(), "Player 1's target base should contain 10 nodes.");
    }

    @Test
    void testInTargetBase() {
        String targetNode = board.getTargetBase(1).get(0);
        assertTrue(board.inTargetBase(1, targetNode), "The node should be in Player 1's target base.");
        assertFalse(board.inTargetBase(1, "20:20"), "Node 20:20 should not be in Player 1's target base.");
    }
}
