package me.rspacerr.paintball.games;

import me.rspacerr.paintball.PaintballPlayer;

import java.util.Map;
import java.util.UUID;

public abstract class Game {
    private Map<UUID, PaintballPlayer> players;

    public abstract void start();
    public abstract void end();
}
