package org.example.Server.Commands;

import org.example.Game.GUI.BoardDisplay;
import org.example.Game.Game;
import org.example.Server.States.InGameState;
import org.example.Server.UserSession;

public class JoinCommand implements Command {
    @Override
    public void execute(UserSession session, String[] args) {
        System.out.println("User " + session.getPlayer().getName() + " chose to join a game.");
        session.sendMessage("You chose to join a game.");

        String playerName = session.askForPlayerName();
        session.getPlayer().setName(playerName);
        System.out.println("User set name to " + playerName);

        // Attempt to find the requested lobby
        Game game = null;
        while (game == null) {
            String lobbyName = session.askForLobbyName();
            game = session.getServer().findGameByName(lobbyName);
            if (game == null) {
                session.sendMessage("Cannot find game with lobby name '" + lobbyName + "'. Try again.");
                System.out.println("User " + session.getPlayer().getName() + " attempted to join non-existent lobby '" + lobbyName + "'.");
            }
        }

        // Join the game
        synchronized (game) {
            if (game.getPlayers().size() >= game.getMaxPlayers()) {
                session.sendMessage("Game '" + game.getLobbyName() + "' is full. Choose another game.");
                System.out.println("User " + session.getPlayer().getName() + " attempted to join full lobby '" + game.getLobbyName() + "'.");
                return;
            }
            game.addPlayer(session.getPlayer());
            game.broadcastMessage(session.getPlayer().getName() + " has joined the game.");
            System.out.println("User " + session.getPlayer().getName() + " joined game '" + game.getLobbyName() + "'.");

            // Transition to InGameState
            session.setState(new InGameState(game));
        }
    }
}
