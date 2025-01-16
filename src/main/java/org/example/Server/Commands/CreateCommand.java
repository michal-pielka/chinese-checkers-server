package org.example.Server.Commands;

import org.example.Game.Board.StdBoard;
import org.example.Game.Game;
import org.example.Game.GameRules.StdRules;
import org.example.Server.States.InGameState;
import org.example.Server.UserSession;

public class CreateCommand implements Command {
    @Override
    public void execute(UserSession session, String[] args) {
        System.out.println("User " + session.getPlayer().getName() + " chose to create a game.");
        session.sendMessage("You chose to create a game.");

        String lobbyName = session.askForLobbyName();
        if (session.getServer().findGameByName(lobbyName) != null) {
            session.sendMessage("A game with lobby name '" + lobbyName + "' already exists. Try a different name.");
            System.out.println("User " + session.getPlayer().getName() + " attempted to create a lobby with existing name '" + lobbyName + "'.");
            return;
        }

        int maxPlayers = session.askForNumberOfPlayers();
        Game game = new Game(lobbyName, maxPlayers, new StdBoard(maxPlayers), new StdRules());

        session.getServer().addGame(game);
        System.out.println("User " + session.getPlayer().getName() + " created game '" + lobbyName + "', max players: " + maxPlayers);

        String playerName = session.askForPlayerName();
        session.getPlayer().setName(playerName);
        System.out.println("User set name to " + playerName);

        game.addPlayer(session.getPlayer());
        game.broadcastMessage(session.getPlayer().getName() + " has created and joined the game.");
        System.out.println("User " + session.getPlayer().getName() + " joined their own created game '" + lobbyName + "'.");

        // Switch to InGameState
        session.setState(new InGameState(game));
    }
}
