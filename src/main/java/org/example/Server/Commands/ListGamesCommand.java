package org.example.Server.Commands;

import org.example.Server.UserSession;
import org.example.Game.Game;

import java.util.List;

public class ListGamesCommand implements Command {
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
                                    + g.getMaxPlayers() + " players)");
            }
            System.out.println("Listed " + currentGames.size() + " available games to user " + session.getPlayer().getName() + ".");
        }
    }
}
