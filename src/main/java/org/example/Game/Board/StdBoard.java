package org.example.Game.Board;

import java.util.*;

import static java.lang.Math.min;

public class StdBoard implements Board {
    private final Graph graph;
    private final List<List<String>> bases;

    public StdBoard(int players) {
        this.graph = new Graph();
        this.bases = new ArrayList<>();
        for(int i=0; i<6; i++) {
            bases.add(new ArrayList<>());
        }
        generate(players);
    }

    private void generate(int players) {
        addNodes();
        addEdges();
        setBases();
        addPegs(players);
    }

    private void addNodes() {
        for (int i = 0; i <= 3; i++) {
            for (int j = 4; j <= 4 + i; j++) {
                graph.addNode(i, j);
            }
        }

        for (int i = 4; i <= 8; i++) {
            for (int j = i - 4; j <= 12; j++) {
                graph.addNode(i, j);
            }
        }

        for (int i = 9; i <= 12; i++) {
            for (int j = 4; j <= 13 + i - 9; j++) {
                graph.addNode(i, j);
            }
        }

        for (int i = 13; i <= 16; i++) {
            for (int j = 9 - 13 + i; j <= 12; j++) {
                graph.addNode(i, j);
            }
        }
    }

    private void addEdges() {
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                graph.addEdge(i, j, i, j + 1);
                graph.addEdge(i, j, i + 1, j + 1);
                graph.addEdge(i, j, i + 1, j);
            }
        }
    }

    private void setBases() {
        Collections.addAll(bases.get(0), "4:0", "4:1", "5:1", "4:2", "5:2", "6:2", "4:3", "5:3", "6:3", "7:3");
        setBase(1, bases.get(0));

        Collections.addAll(bases.get(1), "0:4", "1:4", "1:5", "2:4", "2:5", "2:6", "3:4", "3:5", "3:6", "3:7");
        setBase(2, bases.get(1));

        Collections.addAll(bases.get(2), "4:9", "4:10", "4:11", "4:12", "5:10", "5:11", "5:12", "6:11", "6:12", "7:12");
        setBase(3, bases.get(2));

        Collections.addAll(bases.get(3), "9:13", "10:13", "10:14", "11:13", "11:14", "11:15", "12:13", "12:14", "12:15", "12:16");
        setBase(4, bases.get(3));

        Collections.addAll(bases.get(4), "13:9", "13:10", "13:11", "13:12", "14:10", "14:11", "14:12", "15:11", "15:12", "16:12");
        setBase(5, bases.get(4));

        Collections.addAll(bases.get(5), "9:4", "10:4", "10:5", "11:4", "11:5", "11:6", "12:4", "12:5", "12:6", "12:7");
        setBase(6, bases.get(5));
    }

    private void setBase(int player, List<String> keys) {
        for (String key : keys) {
            if (graph.containsNode(key)) {
                graph.getNode(key).setBase(player);
            }
        }
    }

    private void addPegs(int players) {
        int[][] configurations = {
                {1, 4},
                {1, 3, 5},
                {1, 2, 4, 5},
                {1, 2, 3, 4, 5, 6}
        };

        int[] config = configurations[min(players - 2, configurations.length - 1)];
        for (int i = 0; i < config.length; i++) {
            for (String key : bases.get(config[i] - 1)) {
                graph.getNode(key).setPlayer(i + 1);
            }
        }
    }

    @Override
    public void movePeg(int x1, int y1, int x2, int y2) {
        Node node1 = graph.getNode(x1+":"+y1);
        Node node2 = graph.getNode(x2+":"+y2);
        if(node1 != null && node2 !=null) {
            node2.setPlayer(node1.getPlayer());
            node1.setPlayer(0);
        }
    }

    @Override
    public Node getNode(String key) {
        return graph.getNode(key);
    }

    @Override
    public boolean containsNode(String key) {
        return graph.containsNode(key);
    }

    @Override
    public List<String> getBase(int baseNumber) {return bases.get(baseNumber-1);}

}