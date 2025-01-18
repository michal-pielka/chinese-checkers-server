package org.example.Game.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a graph structure for the game board.
 * Manages nodes and their connections.
 */
public class Graph {
    Map<String, Node> nodes;

    /**
     * Constructs an empty graph.
     */
    public Graph() {
        nodes = new HashMap<>();
    }

    /**
     * Adds a node to the graph at the specified coordinates.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     */
    public void addNode(int x, int y) {
        String key = getKey(x, y);

        if(!nodes.containsKey(key)) {
            Node newNode = new Node(x,y);
            nodes.put(key, newNode);
            newNode.setPlayer(0);
        }
    }

    /**
     * Retrieves a node by its key.
     *
     * @param key the key of the node in the format "x:y".
     * @return the {@link Node} object, or null if it does not exist.
     */
    public Node getNode(String key) {
       return nodes.getOrDefault(key, null);
    }

    /**
     * Checks if a node exists in the graph at the specified key.
     *
     * @param key the key representing the node (format: "x:y").
     * @return true if the node exists, false otherwise.
     */
    public boolean containsNode(String key) {
        return nodes.containsKey(key);
    }

    /**
     * Adds an undirected edge between two nodes in the graph.
     * If either node does not exist, the edge is not added.
     *
     * @param x1 the x-coordinate of the first node.
     * @param y1 the y-coordinate of the first node.
     * @param x2 the x-coordinate of the second node.
     * @param y2 the y-coordinate of the second node.
     */
    public void addEdge(int x1, int y1, int x2, int y2) {
        String key1 = getKey(x1, y1);
        String key2 = getKey(x2, y2);
        if(nodes.containsKey(key1) && nodes.containsKey(key2)) {
            nodes.get(key1).addNeighbour(nodes.get(key2));
            nodes.get(key2).addNeighbour(nodes.get(key1));
        }
    }

    /**
     * Generates a unique key for a node based on its coordinates.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @return a string key in the format "x:y".
     */
    private String getKey(int x, int y) {
        return x + ":" + y;
    }

    public Map<String,Node> getNodes() {
        return nodes;
    }
}
