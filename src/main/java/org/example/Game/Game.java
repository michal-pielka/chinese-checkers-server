package org.example.Game;

import org.example.Game.Board.Board;
import org.example.Game.GameRules.GameRules;
import org.example.Game.GameState.GameState;
import org.example.Game.GameState.WaitingForPlayers;
import java.util.ArrayList;

/**
 * Represents a game session, managing players, the board, game rules, and state transitions.
 */
public class Game {

    /**
     * The name of the game lobby.
     */
    String lobbyName;

    /**
     * The list of players participating in the game.
     */
    ArrayList<Player> players;

    /**
     * The index of the current player in the players list.
     */
    int currentPlayer;

    /**
     * The maximum number of players allowed in the game.
     */
    int maxPlayers;

    /**
     * The game board.
     */
    Board board;

    /**
     * The current state of the game (e.g., waiting for players, in progress, etc.).
     */
    GameState state;

    /**
     * The rules of the game (standard or super).
     */
    GameRules rules;

    /**
     * Constructs a new Game instance.
     *
     * @param name        the name of the game lobby
     * @param playerCount the maximum number of players
     * @param board       the game board
     * @param rules       the rules of the game
     */
    public Game(String name, int playerCount, Board board, GameRules rules) {
        this.lobbyName = name;
        this.maxPlayers = playerCount;
        players = new ArrayList<>();
        this.board = board;
        state = new WaitingForPlayers();
        this.rules = rules;
        currentPlayer = 0;
    }

    /**
     * Gets the name of the game lobby.
     *
     * @return the lobby name
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Gets the list of players in the game.
     *
     * @return the list of players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the index of the current player.
     *
     * @return the current player's index
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player by index.
     *
     * @param i the index of the current player
     */
    public void setCurrentPlayer(int i) {
        this.currentPlayer = i % this.maxPlayers;
    }

    /**
     * Gets the maximum number of players allowed in the game.
     *
     * @return the maximum number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Sets the current game state.
     *
     * @param state the new game state
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Gets the current game state.
     *
     * @return the current game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        state.addPlayer(this, player);
    }

    /**
     * Sends a message to all players in the game.
     *
     * @param message the message to broadcast
     */
    public void broadcastMessage(String message) {
        for(Player player : players) {
            player.sendMessage(message);
        }
    }

    /**
     * Gets the game board.
     *
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the game rules.
     *
     * @return the game rules
     */
    public GameRules getRules() {
        return rules;
    }

    /**
     * Processes a move attempt by a player.
     *
     * @param player the player attempting the move
     * @param x1     the starting x-coordinate
     * @param y1     the starting y-coordinate
     * @param x2     the ending x-coordinate
     * @param y2     the ending y-coordinate
     */
    public void move(Player player, int x1, int y1, int x2, int y2) {
        if(players.get(currentPlayer) != player) {
            player.sendMessage("Not your turn.");
        }
        else {
            state.play(this, x1, y1, x2, y2);
        }
    }

    /**
     * Ends the current player's turn and advances to the next player.
     */
    public void endTurn() {
        currentPlayer = (currentPlayer + 1) % maxPlayers;
    }
}
