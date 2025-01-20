package org.example.Game.Board;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node on the game board.
 * Each node has coordinates, a player occupant, and connections to neighboring nodes.
 */
public class Node {

    /**
     * The x-coordinate of this node.
     */
    private final int x;

    /**
     * The y-coordinate of this node.
     */
    private final int y;

    /**
     * An IntegerProperty storing which player currently occupies this node (0 if unoccupied).
     */
    private final IntegerProperty player;

    /**
     * A list of neighboring nodes that are directly adjacent to this node.
     */
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

    /**
     * Exposes the JavaFX IntegerProperty for the player occupant,
     * allowing observers to bind and listen for changes.
     *
     * @return the IntegerProperty representing the occupant ID.
     */
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

