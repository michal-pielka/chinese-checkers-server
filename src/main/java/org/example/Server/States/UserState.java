package org.example.Server.States;

import org.example.Server.UserSession;
import org.example.Server.Commands.Command;

import java.util.HashMap;

public abstract class UserState {
    protected HashMap<String, Command> commands;

    public abstract void handleCommand(UserSession session, String commandLine);
}
