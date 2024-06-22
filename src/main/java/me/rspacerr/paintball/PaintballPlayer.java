package me.rspacerr.paintball;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

/**
 * Player class. Represents a player who is playing in a paintball game.
 */
public class PaintballPlayer {
    private Player player;
    private long cooldown = 0;
    private int kills = 0;
    private Team team;

    public PaintballPlayer(Player p) {
       this.player = p;
    }

    public void changeTeam(Team t) {
        t.addEntity(player);
    }

    public long cooldown() { return cooldown; }
    public Player player() { return player; }
    public int kills() { return kills; }
}
