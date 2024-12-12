package org.example.Server.Commands;

import org.example.Server.UserThread;

public class MoveCommand implements Command {
    @Override
    public void execute(UserThread user, String[] args) {
        if (args.length != 3) {
            user.sendMessage("Usage: move <startPos> <endPos>");
            return;
        }
        try {
            int startPos = Integer.parseInt(args[1]);
            int endPos = Integer.parseInt(args[2]);
            user.handleMove(startPos, endPos);
        }
        catch(NumberFormatException e) {
            user.sendMessage("Start and End positions should be integers.");
        }
        // TODO: implement handleMove with GameState validation
    }
}

