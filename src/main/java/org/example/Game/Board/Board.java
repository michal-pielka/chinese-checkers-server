package org.example.Game.Board;

import java.util.List;

public interface Board {

    void movePeg(int x1, int y1, int x2, int y2);
    Node getNode(String key);
    boolean containsNode(String key);
    List<String> getBase(int baseNumber);
}
