package org.example.Server.Commands;

import org.example.Server.UserSession;
import org.example.Game.Game;

public class MoveCommand implements Command {
    @Override
    public void execute(UserSession session, String[] args) {
        if (args.length != 3) {
            session.sendMessage("Usage: move <x1:y1> <x2:y2>");
            return;
        }
        try {
            String startPos = args[1];
            String endPos = args[2];
            Game game = ((org.example.Server.States.InGameState) session.getState()).getGame();
            try {
                int x1,y1,x2,y2;

                String[] parts1= startPos.split(":");
                x1 = Integer.parseInt(parts1[0]);
                y1 = Integer.parseInt(parts1[1]);

                String[] parts2 = endPos.split(":");
                x2 = Integer.parseInt(parts2[0]);
                y2 = Integer.parseInt(parts2[1]);
                game.move(session.getPlayer(), x1, y1, x2, y2);
            }
            catch(NumberFormatException e) {
                session.sendMessage("Start and End positions should be in format 'x:y'");
            }

        } catch (NumberFormatException e) {
            session.sendMessage("Start and End positions should be in format 'x:y'");
        } catch (ClassCastException e) {
            session.sendMessage("Invalid game state for moving.");
            System.out.println("User " + session.getPlayer().getName() + " attempted to move in an invalid state.");
        }
    }
}
