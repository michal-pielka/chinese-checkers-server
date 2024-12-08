package org.example.Game;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player {
    private PrintWriter outputWriter;
    private Scanner scanner;
    private String playerName;

    public Player(String name, PrintWriter writer, Scanner scanner ) {
        this.playerName = name;
        this.outputWriter = writer;
        this.scanner = scanner;
    }

    public void sendMessage(String message) {
        outputWriter.println(message);
    }
}
