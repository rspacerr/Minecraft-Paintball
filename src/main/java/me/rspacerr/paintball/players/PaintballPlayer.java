package me.rspacerr.paintball.players;

import me.rspacerr.paintball.PaintballPlugin;
import me.rspacerr.paintball.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Player class. Represents a player who is playing in a paintball game.
 */
public class PaintballPlayer extends GamePlayer {
    private long cooldown = 0;
    private int kills = 0;

    private Scoreboard board = PaintballPlugin.manager.getNewScoreboard();
    private GameTeam paintballTeam = null;
    private Team scoreboardTeam = null;

    public PaintballPlayer(Player p) {
        super(p);
    }

    public long cooldown() { return cooldown; }
}
