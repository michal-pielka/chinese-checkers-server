package org.example.Server.Commands;

import org.example.Server.UserSession;

public interface Command {
    void execute(UserSession session, String[] args);
}
