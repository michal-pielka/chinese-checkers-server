package org.example.Server.States;

import org.example.Server.UserThread;
import org.example.Server.Commands.Command;

import java.util.Map;

public abstract class UserState {
    protected Map<String, Command> commands;

    public abstract void handleCommand(UserThread user, String commandLine);
}