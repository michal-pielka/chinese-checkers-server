package org.example.Server.Commands;

import org.example.Game.GameRules.GameRules;
import org.example.Game.GameRules.StdRules;
import org.example.Game.GameRules.SuperRules;
import org.example.Server.UserSession;
import org.example.Game.Game;

import java.util.List;

/**
 * Handles the 'list' command, allowing the user to see all available games.
 */
public class ListGamesCommand implements Command {

    /**
     * Executes the list command: displays all current games on the server
     * with their lobby names, occupancy, and variant.
     *
     * @param session The UserSession executing this command.
     * @param args    The command arguments (not used directly).
     */
    @Override
    public void execute(UserSession session, String[] args) {
        System.out.println("User " + session.getPlayer().getName() + " requested to list available games.");
        List<Game> currentGames = session.getServer().getGames();

        if (currentGames.isEmpty()) {
            session.sendMessage("No available games to join. You can create one using the 'create' command.");
            System.out.println("No available games found for user " + session.getPlayer().getName() + ".");
        } else {
            session.sendMessage("Available Games:");
            for (Game g : currentGames) {
                session.sendMessage("- " + g.getLobbyName() + " ("
                                    + g.getPlayers().size() + "/"
                                    + g.getMaxPlayers() + " players, variant: "
                                    + GameRulesToString(g.getRules()) + ")");
            }
            System.out.println("Listed " + currentGames.size() + " available games to user " + session.getPlayer().getName() + ".");
        }
    }

    /**
     * Converts a GameRules object into a human-readable string ("Standard", "Super", or "Unknown").
     *
     * @param rules The GameRules object to convert.
     * @return A string describing the game variant.
     */
    private String GameRulesToString(GameRules rules) {
        if(rules instanceof StdRules) {
            return "Standard";
        }
        if(rules instanceof SuperRules) {
            return "Super";
        }
        return "Unknown";
    }
}
