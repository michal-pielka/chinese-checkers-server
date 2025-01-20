package org.example.Game.Board;

import java.util.*;

import static java.lang.Math.min;

/**
 * Represents a standard implementation of the game board.
 * This class manages the graph structure of the board, player positions, and base configurations.
 */
public class StdBoard implements Board {

    /**
     * The underlying graph structure to hold nodes and edges.
     */
    private final Graph graph;

    /**
     * A list of six "bases", each is a list of string keys representing node positions.
     */
    private final List<List<String>> bases;

    /**
     * The number of players in this game (2, 3, 4, or 6).
     */
    private final int players;

    /**
     * Constructs a standard board for the game with the specified number of players.
     *
     * @param players the number of players in the game.
     */
    public StdBoard(int players) {
        this.graph = new Graph();
        this.bases = new ArrayList<>();
        for(int i=0; i<6; i++) {
            bases.add(new ArrayList<>());
        }
        this.players = players;
        generate();
    }

    /**
     * Performs the overall generation of the board: adding nodes,
     * edges, bases, and initial pegs according to the number of players.
     */
    private void generate() {
        addNodes();
        addEdges();
        setBases();
        addPegs();
    }

    /**
     * Adds nodes to the board to represent positions.
     */
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

    /**
     * Adds edges between nodes to define the connections on the board.
     */
    private void addEdges() {
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                graph.addEdge(i, j, i, j + 1);
                graph.addEdge(i, j, i + 1, j + 1);
                graph.addEdge(i, j, i + 1, j);
            }
        }
    }

    /**
     * Sets up the six possible bases for a 6-pointed star board,
     * defining which node keys belong to each base.
     */
    private void setBases() {
        Collections.addAll(bases.get(0), "4:0", "4:1", "5:1", "4:2", "5:2", "6:2", "4:3", "5:3", "6:3", "7:3");
        Collections.addAll(bases.get(1), "9:4", "10:4", "10:5", "11:4", "11:5", "11:6", "12:4", "12:5", "12:6", "12:7");
        Collections.addAll(bases.get(2), "13:9", "13:10", "13:11", "13:12", "14:10", "14:11", "14:12", "15:11", "15:12", "16:12");
        Collections.addAll(bases.get(3), "9:13", "10:13", "10:14", "11:13", "11:14", "11:15", "12:13", "12:14", "12:15", "12:16");
        Collections.addAll(bases.get(4), "4:9", "4:10", "4:11", "4:12", "5:10", "5:11", "5:12", "6:11", "6:12", "7:12");
        Collections.addAll(bases.get(5), "0:4", "1:4", "1:5", "2:4", "2:5", "2:6", "3:4", "3:5", "3:6", "3:7");
    }

    /**
     * Places initial pegs in each base for the starting configuration,
     * depending on the number of players.
     */
    private void addPegs() {
        int[] config = getStartConfiguration();
        for (int i = 0; i < config.length; i++) {
            for (String key : bases.get(config[i] - 1)) {
                graph.getNode(key).setPlayer(i + 1);
            }
        }
    }

    /**
     * Determines the start configuration for the given number of players,
     * returning an array of base indices that should be occupied.
     *
     * @return an array of base indices (1-based).
     */
    private int[] getStartConfiguration() {
        int[][] configurations = {
                {1, 4},
                {1, 3, 5},
                {1, 2, 4, 5},
                {1, 2, 3, 4, 5, 6}
        };

        return configurations[min(players-2, configurations.length-1)];
    }

    /**
     * Determines the target base configuration for each player, used to check for a win.
     *
     * @return an array of base indices (1-based) that represents the target for each player.
     */
    private int[] getTargetConfiguration() {
        int[][] configurations ={
                {4, 1},
                {4, 6, 2},
                {4, 5, 1, 2},
                {4, 5, 6, 1, 2, 3}
        };

        return configurations[min(players-2, configurations.length-1)];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String,Node> getNodes() {
        return graph.getNodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void movePeg(int x1, int y1, int x2, int y2) {
        Node node1 = graph.getNode(x1+":"+y1);
        Node node2 = graph.getNode(x2+":"+y2);
        if(node1 != null && node2 !=null) {
            node2.setPlayer(node1.getPlayer());
            node1.setPlayer(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNode(String key) {
        return graph.getNode(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsNode(String key) {
        return graph.containsNode(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getTargetBase(int player) {
        int[] config = getTargetConfiguration();
        return bases.get(config[player-1] - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean inTargetBase(int player, String key) {
        int[] config = getTargetConfiguration();
        if(player == 0) {
            return false;
        }
        int targetBase = config[player-1];
        if(bases.get(targetBase-1).contains(key)) {
            return true;
        }
        return false;
    }
}
