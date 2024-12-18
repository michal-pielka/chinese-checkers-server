package org.example.Server.Commands;

import org.example.Server.UserThread;

public class CreateCommand implements Command {
    @Override
    public void execute(UserThread user, String[] args) {
        user.handleCreate();
    }
}