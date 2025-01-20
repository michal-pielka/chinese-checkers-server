package org.example.Game.Board;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node on the game board.
 * Each node has coordinates, a player, a base assignment, and connections to neighboring nodes.
 */
public class Node {
     private final int x;
     private final int y;
     private final IntegerProperty player;
     public List<Node> neighbours;

    /**
     * Constructs a node with the specified coordinates.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbours = new ArrayList<>();
        this.player = new SimpleIntegerProperty(0);
    }

    /**
     * Adds a neighboring node to this node.
     *
     * @param neighbour the neighboring node to add.
     */
    public void addNeighbour(Node neighbour) {
        if(!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    /**
     * Sets the ID of the player occupying this node.
     *
     * @param x the ID of the player (0 if unoccupied).
     */
    public void setPlayer(int x) {
        this.player.set(x);
    }

    /**
     * Retrieves the ID of the player currently occupying this node.
     *
     * @return the player ID (0 if unoccupied).
     */
    public int getPlayer() {
        return player.get();
    }

    public IntegerProperty playerProperty() {
        return player;
    }
    /**
     * Retrieves the x-coordinate of this node.
     *
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of this node.
     *
     * @return the y-coordinate.
     */
    public int getY() {
        return y;
    }
}
