package me.rspacerr.paintball;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Player class. Represents a player who is playing in a paintball game.
 */
public class PaintballPlayer {
    private Player player;
    private long cooldown = 0;
    private int kills = 0;

    private Scoreboard board = PaintballPlugin.manager.getNewScoreboard();
    private PaintballTeam paintballTeam = null;
    private Team scoreboardTeam = null;

    public PaintballPlayer(Player p) {
       this.player = p;
    }

    public void changeTeam(String s) {
        // if already on a team, remove them from their previous team
        if (paintballTeam != null) {
            paintballTeam.removePlayer(this);
        }

        // add player to scoreboard team
        if (board.getTeam(s) == null) {
            scoreboardTeam = board.registerNewTeam(s);
            scoreboardTeam.setAllowFriendlyFire(false);
            scoreboardTeam.setPrefix("[" + s + "]");

            paintballTeam = new PaintballTeam(s);
            paintballTeam.addPlayer(this);
        }
        scoreboardTeam.addPlayer(player);

        // TODO: add team to all other player's scoreboards
    }

    /*
    public static PaintballPlayer getPaintballPlayer(Player p) {
        // TODO
    }
     */

    public long cooldown() { return cooldown; }
    public Player player() { return player; }
    public int kills() { return kills; }
    public void incrementKills() { ++kills; }
}
