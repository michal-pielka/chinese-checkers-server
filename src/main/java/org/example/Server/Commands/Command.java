package org.example.Server.Commands;

import org.example.Server.UserThread;

public interface Command {
    void execute(UserThread user, String[] args);
}
