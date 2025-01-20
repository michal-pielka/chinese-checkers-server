package org.example.Game.GameRules;

import org.example.Game.Board.Board;
import org.example.Game.Board.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An advanced ("super") implementation of the game rules,
 * allowing long jumps and requiring line-clear checks.
 */
public class SuperRules implements GameRules {

    /**
     * Checks if all nodes on the line between (startX,startY) and (endX,endY)
     * are empty, i.e., no other player's piece is in the direct line path.
     *
     * @param board  the game board
     * @param dx     the step in the x-direction
     * @param dy     the step in the y-direction
     * @param startX starting x-coordinate
     * @param startY starting y-coordinate
     * @param endX   ending x-coordinate
     * @param endY   ending y-coordinate
     * @return true if the line is clear, false if a piece is encountered
     */
    public boolean lineClear(Board board, int dx, int dy, int startX, int startY, int endX, int endY) {
        int checkX = startX + dx;
        int checkY = startY + dy;
        while(checkX != endX || checkY != endY) {
            if(board.getNode(checkX+":"+checkY).getPlayer() != 0) {
                return false;
            }
            checkX += dx;
            checkY += dy;
        }
        return true;
    }

    /**
     * Recursively finds all possible jump destinations for a piece located at (x,y).
     * "Super" jumps allow skipping multiple pieces in a straight line if the path is clear.
     *
     * @param board   the game board
     * @param x       current x-coordinate
     * @param y       current y-coordinate
     * @param visited set of nodes already visited in the jump chain
     * @return a set of node keys (x:y) that are reachable via jumps
     */
    public Set<String> findJumps(Board board, int x, int y, Set<String> visited) {
        String currKey = x+":"+y;
        visited.add(currKey);

        int[] directionsX = {0, 1, 1, 0, -1, -1};
        int[] directionsY = {1, 1, 0, -1, -1, 0};

        for(int i=0 ; i<6; i++) {
            int dx = directionsX[i];
            int dy = directionsY[i];

            int midX = x + dx;
            int midY = y + dy;
            Node midNode;
            int jumpX = x + 2*dx;
            int jumpY = y + 2*dy;
            Node jumpNode;

            boolean foundOne = false;
            while(board.containsNode(jumpX+":"+jumpY) && !foundOne) {
                midNode = board.getNode(midX +":"+midY);
                jumpNode = board.getNode(jumpX+":"+jumpY);
                if(midNode.getPlayer() != 0) {
                    foundOne = true;
                    if(lineClear(board,dx,dy,midX,midY,jumpX,jumpY) && jumpNode.getPlayer() == 0 && !visited.contains(jumpX+":"+jumpY)) {
                        visited.add(jumpX+":"+jumpY);
                        findJumps(board, jumpX, jumpY, visited);
                    }
                }

                midX += dx;
                midY += dy;
                jumpX += 2*dx;
                jumpY += 2*dy;
            }
        }

        return visited;
    }

    /**
     * Checks whether a move is valid under the "Super" rules, including adjacency moves
     * or multi-step jumps across pieces in a straight line.
     *
     * @param board  the game board on which the move is being made
     * @param player the player attempting the move
     * @param x1     the x-coordinate of the starting position
     * @param y1     the y-coordinate of the starting position
     * @param x2     the x-coordinate of the destination position
     * @param y2     the y-coordinate of the destination position
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean isMoveValid(Board board, int player, int x1, int y1, int x2, int y2) {
        String key1 = x1 + ":" + y1;
        String key2 = x2 + ":" + y2;
        Node node1, node2;

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
     * @param board  the game board
     * @param player the player to check
     * @return true if the player has met the winning condition, false otherwise
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

