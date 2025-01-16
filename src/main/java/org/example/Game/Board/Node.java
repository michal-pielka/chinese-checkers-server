package org.example.Game.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node on the game board.
 * Each node has coordinates, a player, a base assignment, and connections to neighboring nodes.
 */
public class Node {
     private final int x;
     private final int y;
     private int player;
     private int base;
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
        this.player = 0;
        this.base = 0;
        this.neighbours = new ArrayList<>();
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
        this.player = x;
    }

    /**
     * Retrieves the ID of the player currently occupying this node.
     *
     * @return the player ID (0 if unoccupied).
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Assigns this node to a specific base.
     *
     * @param x the ID of the base.
     */
    public void setBase(int x) {
        this.base = x;
    }

    /**
     * Retrieves the ID of the base this node belongs to.
     *
     * @return the base ID (0 if not part of a base).
     */
    public int getBase() {
        return base;
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
