package org.example.Game;

import org.example.Game.Board;
import org.example.Game.GameState.GameState;
import org.example.Game.GameState.WaitingForPlayers;
import org.example.Game.Player;

import java.util.ArrayList;

public class Game {
    private String lobbyName;
    private ArrayList<Player> players;
    private int currentPlayer;
    private int maxPlayers;
    private Board board;
    private GameState state;

    public Game(String name, int playerCount) {
        this.lobbyName = name;
        this.maxPlayers = playerCount;
        this.players = new ArrayList<>();
        this.board = null; // TODO: Initialize properly
        this.state = new WaitingForPlayers();
        this.currentPlayer = 0;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int i) {
        this.currentPlayer = i % this.maxPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public void addPlayer(Player player) {
        state.addPlayer(this, player);
    }

    public void broadcastMessage(String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public void move(Player player, int startPos, int endPos) {
        if (players.get(currentPlayer) != player) {
            player.sendMessage("Not your turn.");
        } else {
            state.play(this, startPos, endPos);
        }
    }

    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % maxPlayers;
    }
}
