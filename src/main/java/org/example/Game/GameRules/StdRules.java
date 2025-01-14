package org.example.Game.GameRules;

import org.example.Game.Board.Board;
import org.example.Game.Board.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StdRules implements GameRules{
    Board board;

    public Set<String> findJumps(int x, int y, Set<String> visited) {
        String currKey = x + ":" + y;
        visited.add(currKey);

        Node currNode = board.getNode(currKey);

        for(Node neighbour : currNode.neighbours) {
            if(neighbour.getPlayer() != 0) {
                int jumpX = 2 * (neighbour.getX() - x);
                int jumpY = 2 * (neighbour.getY() - y);
                String jumpKey = jumpX + ":" + jumpY;

                if(board.containsNode(jumpKey)) {
                    if(board.getNode(jumpKey).getPlayer() == 0 && !visited.contains(jumpKey)) {
                        visited.add(jumpKey);
                        findJumps(jumpX, jumpY, visited);
                    }
                }
            }
        }
        return visited;
    }

    @Override
    public boolean isMoveValid(int x1, int y1, int x2, int y2) {
        String key1 = x1 + ":" + y1;
        String key2 = x2 + ":" + y2;
        Node node1, node2;
        if(board.containsNode(key1) && board.containsNode(key2)) {
            node1 = board.getNode(key1);
            node2 = board.getNode(key2);
            if(node2.getBase() == 0 || node2.getBase() == (node1.getPlayer() + 2)%6 + 1) {
                if(node1.neighbours.contains(node2) && node2.getPlayer()==0) {
                    return true;
                }
                else {
                    Set<String> possibleJumps = findJumps(x1, y1, new HashSet<>());
                    return possibleJumps.contains(key2);
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkForWin(int player) {
        List<String> targetBase = board.getBase((player+2)%6 + 1);
        for(String key: targetBase) {
            if(board.getNode(key).getPlayer() != player) {
                return false;
            }
        }
        return true;
    }
}
