package org.example.Server.Commands;

import org.example.Server.UserSession;
import org.example.Game.Game;

public class MoveCommand implements Command {
    @Override
    public void execute(UserSession session, String[] args) {
        if (args.length != 3) {
            session.sendMessage("Usage: move <startPos> <endPos>");
            return;
        }
        try {
            int startPos = Integer.parseInt(args[1]);
            int endPos = Integer.parseInt(args[2]);
            Game game = ((org.example.Server.States.InGameState) session.getState()).getGame();
            game.move(session.getPlayer(), startPos, endPos);
        } catch (NumberFormatException e) {
            session.sendMessage("Start and End positions should be integers.");
        } catch (ClassCastException e) {
            session.sendMessage("Invalid game state for moving.");
            System.out.println("User " + session.getPlayer().getName() + " attempted to move in an invalid state.");
        }
    }
}
