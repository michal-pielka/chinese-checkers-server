package org.example.Game;

import org.example.Game.GameState.GameState;
import org.example.Game.GameState.WaitingForPlayers;
import java.util.ArrayList;

public class Game {
    String lobbyName;
    ArrayList<Player> players;
    int maxPlayers;
    Board board;
    GameState state;

    public Game(String name, int playerCount) {
        this.lobbyName = name;
        this. maxPlayers = playerCount;
        players = new ArrayList<>();
        board = null; // Do poprawy!!!
        state = new WaitingForPlayers();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void addPlayer(Player player) {
        state.addPlayer(this, player);
    }

    public void broadcastMessage(String message) {
        for(Player player : players) {
            player.sendMessage(message);
        }
    }
}
