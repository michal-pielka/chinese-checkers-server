package org.example.Game.GameRules;

import org.example.Game.Board.Board;
import org.example.Game.Board.Node;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Standard implementation of the game rules.
 * Handles validation of moves and determines winning conditions.
 */
public class StdRules implements GameRules {

    /**
     * Finds all valid jump destinations from the given starting node.
     *
     * @param board the game board.
     * @param x     X position of starting node.
     * @param y     Y position of starting node
     * @param visited nodes already visited (new HashSet<> for default usage).
     * @return a set of node keys (x:y) that can be jumped to.
     */
    @Override
    public Set<String> findJumps(Board board, int x, int y, Set<String> visited) {
        String currKey = x + ":" + y;
        visited.add(currKey);

        Node currNode = board.getNode(currKey);

        for(Node neighbour : currNode.neighbours) {
            if(neighbour.getPlayer() != 0) {
                int jumpX = 2 * (neighbour.getX() - x) + x;
                int jumpY = 2 * (neighbour.getY() - y) + y;
                String jumpKey = jumpX + ":" + jumpY;

                if(board.containsNode(jumpKey)) {
                    if(board.getNode(jumpKey).getPlayer() == 0 && !visited.contains(jumpKey)) {
                        visited.add(jumpKey);
                        findJumps(board, jumpX, jumpY, visited);
                    }
                }
            }
        }
        return visited;
    }

    /**
     * Checks whether a move is valid according to standard rules:
     * single-step adjacency or multi-step jumps.
     *
     * @param board  the game board on which the move is being made.
     * @param player the player attempting the move.
     * @param x1     the x-coordinate of the starting position.
     * @param y1     the y-coordinate of the starting position.
     * @param x2     the x-coordinate of the destination position.
     * @param y2     the y-coordinate of the destination position.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isMoveValid(Board board, int player, int x1, int y1, int x2, int y2) {
        String key1 = x1 + ":" + y1;
        String key2 = x2 + ":" + y2;
        Node node1, node2;

        if(x1 == x2 && y1 == y2) {
            return false;
        }
        if(board.containsNode(key1) && board.containsNode(key2)) {
            node1 = board.getNode(key1);
            node2 = board.getNode(key2);
            if(node1.getPlayer() != player) {
                return false;
            }
            if(board.inTargetBase(player, key1) && !board.inTargetBase(player, key2)) {
                return false;
            }

            // Single-step adjacency
            if(node1.neighbours.contains(node2) && node2.getPlayer()==0) {
                return true;
            }
            else {
                // Multi-step jumps
                Set<String> possibleJumps = findJumps(board, x1, y1, new HashSet<>());
                return possibleJumps.contains(key2);
            }
        }
        return false;
    }

    /**
     * Checks if the specified player has all of their pieces in their target base.
     *
     * @param board  the game board.
     * @param player the player to check for a win.
     * @return true if the player has won, false otherwise.
     */
    @Override
    public boolean checkForWin(Board board, int player) {
        List<String> targetBase = board.getTargetBase(player);
        for(String key: targetBase) {
            if(board.getNode(key).getPlayer() != player) {
                return false;
            }
        }
        return true;
    }
}
