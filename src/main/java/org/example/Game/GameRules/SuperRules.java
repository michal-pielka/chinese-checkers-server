package org.example.Game.GameRules;

import org.example.Game.Board.Board;
import org.example.Game.Board.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuperRules implements GameRules{

    private boolean lineClear(Board board, int dx, int dy, int startX, int startY, int endX, int endY) {
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

    private Set<String> findJumps(Board board, int x, int y, Set<String> visited) {
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

            if(node1.neighbours.contains(node2) && node2.getPlayer()==0) {
                return true;
            }
            else {
                Set<String> possibleJumps = findJumps(board, x1, y1, new HashSet<>());
                return possibleJumps.contains(key2);
            }
        }
        return false;
    }

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
