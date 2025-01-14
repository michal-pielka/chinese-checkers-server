package org.example.Game.Board;

public interface Board {
    void movePeg(int x1, int y1, int x2, int y2);
    Node getNode(String key);
    boolean containsNode(String key);
}
