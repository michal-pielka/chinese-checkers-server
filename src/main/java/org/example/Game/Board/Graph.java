package org.example.Game.Board;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    Map<String, Node> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }

    public void addNode(int x, int y) {
        String key = getKey(x, y);

        if(!nodes.containsKey(key)) {
            Node newNode = new Node(x,y);
            nodes.put(key, newNode);
        }
    }

    public Node getNode(String key) {
       return nodes.getOrDefault(key, null);
    }

    public boolean containsNode(String key) {
        return nodes.containsKey(key);
    }

    public void addEdge(int x1, int y1, int x2, int y2) {
        String key1 = getKey(x1, y1);
        String key2 = getKey(x2, y2);
        if(nodes.containsKey(key1) && nodes.containsKey(key2)) {
            nodes.get(key1).addNeighbour(nodes.get(key2));
            nodes.get(key2).addNeighbour(nodes.get(key1));
        }
    }

    private String getKey(int x, int y) {
        return x + ":" + y;
    }
}
