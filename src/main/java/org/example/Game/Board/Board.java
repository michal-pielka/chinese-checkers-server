package org.example.Game.Board;

import java.util.List;
import java.util.Map;

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
     * Retrieves the map of all nodes in the board, keyed by "x:y".
     *
     * @return a map of node keys to Node objects.
     */
    Map<String,Node> getNodes();

    /**
     * Checks if a node exists at the specified key.
     *
     * @param key the key representing the node in the format "x:y".
     * @return {@code true} if the node exists, {@code false} otherwise.
     */
    boolean containsNode(String key);

    /**
     * Retrieves the target base for the specified player.
     *
     * @param player the 1-based player ID
     * @return a list of node keys that represent the player's target base,
     * key of the node furthest from the center should be at index 0.
     */
    List<String> getTargetBase(int player);

    /**
     * Retrieves the start base for the specified player.
     *
     * @param player the 1-based player ID
     * @return a list of node keys that represent the player's start base
     */
    List<String> getStartBase(int player);

    /**
     * Determines if a given node key is within the target base of the specified player.
     *
     * @param player the 1-based player ID
     * @param key    the node key in the format "x:y"
     * @return true if the node is in the player's target base, false otherwise
     */
    boolean inTargetBase(int player, String key);
}
