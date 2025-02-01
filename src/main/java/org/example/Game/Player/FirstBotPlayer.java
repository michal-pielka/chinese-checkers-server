package org.example.Game.Player;

import org.example.Game.Board.Node;
import org.example.Game.Game;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FirstBotPlayer extends BotPlayer{
    private final int playerNumber;
    private final List<String> pegs;
    private final int furthestX;
    private final int furthestY;

    public FirstBotPlayer(String name, Game game, int playerNumber) {
        super(name, game);
        System.out.println("nazwa i gra ok");
        this.playerNumber = playerNumber;
        System.out.println("player number ok: " + playerNumber);
        this.pegs = game.getBoard().getStartBase(playerNumber);
        System.out.println("pegs ok");
        this.furthestX = this.getFurthestX();
        System.out.println("X ok");
        this.furthestY = this.getFurthestY();
        System.out.println("Y ok");
    }

    @Override
    public void makeMove() {
        boolean goodMove = false;
        String move="";
        String peg="";

        while(!goodMove) {
            peg = getPegToMove();
            move = getTheBestMove(peg);
            if(move != null) {
                goodMove = true;
            }
        }

        int x1 = game.getBoard().getNode(peg).getX();
        int y1 = game.getBoard().getNode(peg).getY();

        try {
            String[] xy = move.split(":");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);

            game.getState().play(game, x1, y1, x, y);
            pegs.set(pegs.indexOf(peg), move);
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private Node getFurthestNode() {
        String key = game.getBoard().getTargetBase(playerNumber).getFirst();
        return game.getBoard().getNode(key);
    }
    private int getFurthestX() {
        return getFurthestNode().getX();
    }

    private int getFurthestY() {;
        return getFurthestNode().getY();
    }

    private double distance(int x, int y) {
        return Math.sqrt(
                Math.pow(x - furthestX, 2)
                        - (x - furthestX) * (y - furthestY)
                        + Math.pow(y - furthestY, 2)
        );
    }

    private String getPegToMove() {
        Random random = new Random();
        int idx =random.nextInt(pegs.size());

        return pegs.get(idx);
    }

    private String getTheBestMove(String peg) {
        Node currNode = game.getBoard().getNode(peg);
        int x = currNode.getX();
        int y = currNode.getY();
        Set<String> jumps = game.getRules().findJumps(game.getBoard(), x, y, new HashSet<>());
        double min = Double.MAX_VALUE;
        String minCoords="";
        Node jumpNode;
        int jumpX;
        int jumpY;
        double dist;

        for(String move : jumps) {
            jumpNode = game.getBoard().getNode(move);
            jumpX = jumpNode.getX();
            jumpY = jumpNode.getY();
            dist = distance(jumpX, jumpY);
            if(min > dist) {
                min = dist;
                minCoords = move;
            }
        }

        for(Node neighbour : currNode.neighbours) {
            jumpX = neighbour.getX();
            jumpY = neighbour.getY();
            dist = distance(jumpX, jumpY);
            if(min > dist) {
                min = dist;
                minCoords = jumpX +":"+jumpY;
            }
        }

        if(min < distance(x,y)) {
            return minCoords;
        }
        return null;
    }
}
