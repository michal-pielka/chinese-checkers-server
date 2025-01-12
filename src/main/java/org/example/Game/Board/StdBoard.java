package org.example.Game.Board;

import java.util.ArrayList;

public class StdBoard implements Board {
    Graph board;

    public StdBoard() {
        this.board = new Graph();
        generate();
    }

    @Override
    public void generate() {
        addNodes();
        addEdges();
        setBases();
    }

    private void addNodes() {
        for(int i=0; i<=3; i++) {
            for(int j=4; j<=4+i; j++) {
                board.addNode(i,j);
            }
        }

        for(int i=4; i<=8; i++) {
            for(int j=i-4; j<=12; j++) {
                board.addNode(i,j);
            }
        }

        for(int i=9; i<=12; i++) {
            for(int j=4; j<=13+i-9; j++){
                board.addNode(i,j);
            }
        }

        for(int i=13; i<=16; i++) {
            for(int j=9-13+i; j<=12; j++) {
                board.addNode(i,j);
            }
        }
    }

    private void addEdges() {
        for(int i=0; i<17; i++) {
            for(int j=0; j<17; j++){
                board.addEdge(i, j, i, j+1);
                board.addEdge(i, j, i+1, j+1);
                board.addEdge(i, j, i+1, j);
            }
        }
    }

    private void setBases() {
        int[][] base1 = {{4,0}, {4,1}, {5,1}, {4,2}, {5,2}, {6,2}, {4,3}, {5,3}, {6,3}, {7,3}};
        board.setBase(1, base1);
        //TODO: add the rest of the bases
    }
}
