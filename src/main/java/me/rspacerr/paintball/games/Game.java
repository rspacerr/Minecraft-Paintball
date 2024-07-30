package me.rspacerr.paintball.games;

import me.rspacerr.paintball.players.GamePlayer;

import java.util.Map;
import java.util.UUID;

public abstract class Game {
    private Map<UUID, GamePlayer> players;

    public abstract void start();
    public abstract void end();
}
