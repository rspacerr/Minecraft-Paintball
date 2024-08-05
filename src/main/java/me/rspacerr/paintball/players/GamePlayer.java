package me.rspacerr.paintball.players;

import me.rspacerr.paintball.PaintballPlugin;
import me.rspacerr.paintball.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GamePlayer {
    private Player player;
    private int kills = 0;

    private Scoreboard board = PaintballPlugin.manager.getNewScoreboard();
    private GameTeam team = null;
    private Team scoreboardTeam = null;

    public GamePlayer(Player p) {
        this.player = p;
    }

    public void changeTeam(String s) {
        // if already on a team, remove them from their previous team
        if (team != null) {
            team.removePlayer(this);
        }

        // add player to scoreboard team
        if (board.getTeam(s) == null) {
            scoreboardTeam = board.registerNewTeam(s);
            scoreboardTeam.setAllowFriendlyFire(false);
            scoreboardTeam.setPrefix("[" + s + "]");

            team = new GameTeam(s);
            team.addPlayer(this);
        }
        scoreboardTeam.addPlayer(player);

        // TODO: add team to all other player's scoreboards

    }

    public Player player() { return player; }
    public int kills() { return kills; }
    public void incrementKills() { ++kills; }
}
