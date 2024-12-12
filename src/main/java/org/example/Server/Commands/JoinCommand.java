package org.example.Server.Commands;

import org.example.Server.UserThread;

public class JoinCommand implements Command {
    @Override
    public void execute(UserThread user, String[] args) {
        user.handleJoin();
    }
}
