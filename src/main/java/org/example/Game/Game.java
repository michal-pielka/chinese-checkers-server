package org.example.Game;

import org.example.Game.Board.Board;
import org.example.Game.GameState.GameState;
import org.example.Game.GameState.WaitingForPlayers;
import java.util.ArrayList;

public class Game {
    String lobbyName;
    ArrayList<Player> players;
    int currentPlayer;
    int maxPlayers;
    Board board;
    GameState state;

    public Game(String name, int playerCount) {
        this.lobbyName = name;
        this. maxPlayers = playerCount;
        players = new ArrayList<>();
        board = null; // Do poprawy!!!
        state = new WaitingForPlayers();
        currentPlayer = 0;
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
        for(Player player : players) {
            player.sendMessage(message);
        }
    }

    public void move(Player player, int startPos, int endPos) {
        if(players.get(currentPlayer) != player) {
            player.sendMessage("Not your turn.");
        }
        else {
            state.play(this, startPos, endPos);
        }
    }

    public void endTurn() {
        currentPlayer=(currentPlayer + 1) % maxPlayers;
    }
}
