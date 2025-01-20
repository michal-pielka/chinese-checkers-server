package org.example.Server.Commands;

import org.example.Game.Board.StdBoard;
import org.example.Game.Game;
import org.example.Game.GameRules.GameRules;
import org.example.Server.States.InGameState;
import org.example.Server.UserSession;

/**
 * Handles the 'create' command, allowing a user to create a new game lobby.
 */
public class CreateCommand implements Command {

    /**
     * Executes the create-game flow:
     * 1. Prompts the user for a lobby name.
     * 2. Checks for duplicate lobby names.
     * 3. Prompts for number of players and game variant.
     * 4. Creates the game, adds it to the server, and transitions the user to the InGameState.
     *
     * @param session The UserSession executing this command.
     * @param args    The command arguments (not used directly here).
     */
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
        GameRules gameVariant = session.askForGameVariant();

        Game game = new Game(lobbyName, maxPlayers, new StdBoard(maxPlayers), gameVariant);
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

