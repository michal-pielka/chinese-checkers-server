package org.example.Server.Commands;

import org.example.Server.UserThread;

public class MoveCommand implements Command {
    @Override
    public void execute(UserThread user, String[] args) {
        if (args.length != 3) {
            user.sendMessage("Usage: move <startPos> <endPos>");
            return;
        }

        // TODO: implement handleMove with GameState validation
    }
}

