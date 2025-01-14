package org.example.Game.Board;

import java.util.ArrayList;
import java.util.List;

public class Node {
     private final int x;
     private final int y;
     private int player;
     private int base;
     public List<Node> neighbours;


    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.player = 0;
        this.base = 0;
        this.neighbours = new ArrayList<>();
    }

    public void addNeighbour(Node neighbour) {
        if(!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    public void setPlayer(int x) {
        this.player = x;
    }

    public int getPlayer() {
        return player;
    }

    public void setBase(int x) {
        this.base = x;
    }

    public int getBase() {
        return base;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
