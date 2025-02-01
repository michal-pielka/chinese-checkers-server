package org.example.Game.Player;

import java.io.PrintWriter;

/**
 * Represents a player in the game.
 */
public class HumanPlayer implements Player{

    /**
     * A PrintWriter to send messages to this player.
     */
    private PrintWriter outputWriter;

    /**
     * The player's name.
     */
    private String playerName;

    /**
     * Constructs a new Player with the specified name and output writer.
     *
     * @param name   the name of the player
     * @param writer the outputWriter for the player
     */
    public HumanPlayer(String name, PrintWriter writer) {
        this.playerName = name;
        this.outputWriter = writer;
    }

    /**
     * Sends a message to the player.
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        outputWriter.println(message);
    }

    /**
     * Gets the player's name.
     *
     * @return the name of the player
     */
    @Override
    public String getName() {
        return playerName;
    }

    /**
     * Sets the player's name.
     *
     * @param name the name of the player
     */
    @Override
    public void setName(String name) {
        this.playerName = name;
    }
}
