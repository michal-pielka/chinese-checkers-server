package org.example.Game.Player;

import org.example.Game.Game;

public abstract class BotPlayer implements Player{
    protected String name;
    protected Game game;

    public BotPlayer(String name, Game game) {
        setName(name);
        this.game = game;
    }

    @Override
    public String getName() { return name; }

    @Override
    public void setName(String name) { this.name = name; }

    public abstract void makeMove();
}
