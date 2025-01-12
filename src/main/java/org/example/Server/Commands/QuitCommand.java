package org.example.Server.Commands;

import org.example.Server.UserThread;

public class QuitCommand implements Command {
    @Override
    public void execute(UserThread user, String[] args) {
        user.handleQuit();
    }
}