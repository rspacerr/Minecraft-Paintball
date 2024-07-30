package me.rspacerr.paintball.games;

import org.bukkit.event.Listener;

public abstract class Game implements Listener {
    public abstract void start();
    public abstract void end();
}
