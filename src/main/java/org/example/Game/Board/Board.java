package org.example.Game.Board;

import java.util.List;

/**
 * Represents the game board.
 */
public interface Board {

    /**
     * Moves a peg from one position to another on the board.
     *
     * @param x1 the x-coordinate of the starting position.
     * @param y1 the y-coordinate of the starting position.
     * @param x2 the x-coordinate of the destination position.
     * @param y2 the y-coordinate of the destination position.
     */
    void movePeg(int x1, int y1, int x2, int y2);

    /**
     * Retrieves the node at the specified key.
     *
     * @param key the key representing the node in the format "x:y".
     * @return the {@link Node} object at the specified key, or null if the node does not exist.
     */
    Node getNode(String key);

    /**
     * Checks if a node exists at the specified key.
     *
     * @param key the key representing the node in the format "x:y".
     * @return {@code true} if the node exists, {@code false} otherwise.
     */
    boolean containsNode(String key);

    /**
     * Retrieves all keys associated with a specific base.
     *
     * @param baseNumber the number of the base (1 through 6).
     * @return a list of strings representing the keys of the nodes in the specified base.
     */
    List<String> getBase(int baseNumber);
}
