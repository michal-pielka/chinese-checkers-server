package org.example.Game;

import java.io.PrintWriter;

public class Player {
    private PrintWriter outputWriter;
    private String playerName;

    public Player(String name, PrintWriter writer) {
        this.playerName = name;
        this.outputWriter = writer;
    }

    public void sendMessage(String message) {
        outputWriter.println(message);
    }

    public String getName() {
        return playerName;
    }

    public void setName(String name) {this.playerName = name;}
}
