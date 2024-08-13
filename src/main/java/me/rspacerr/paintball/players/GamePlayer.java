package me.rspacerr.paintball.players;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.PaintballPlugin;
import me.rspacerr.paintball.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;

public class GamePlayer {
    private Player player;
    private int kills = 0;

    public final Scoreboard board = PaintballPlugin.manager.getNewScoreboard();
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
            scoreboardTeam.setPrefix("[" + s + "] ");

            if (GameManager.getTeam(s) == null) {
                GameManager.addTeam(s);
            }
            team = GameManager.getTeam(s);
            team.addPlayer(this);
            scoreboardTeam.addPlayer(player);
        } else {
            board.getTeam(s).addPlayer(player);
        }

        for (GamePlayer player : GameManager.players()) {
            // add everyone to this player's scoreboard
            if (board.getTeam(player.team()) == null) {
                Team scoreboardTeam = board.registerNewTeam(player.team());
                scoreboardTeam.setPrefix("[" + player.team() + "] ");
                scoreboardTeam.setAllowFriendlyFire(false);
                scoreboardTeam.addPlayer(player.player);
            } else {
                board.getTeam(player.team()).addPlayer(player.player);
            }

            // add this player to everyone else's scoreboard
            if (player.board.getTeam(s) == null) {
                Team scoreboardTeam = player.board.registerNewTeam(s);
                scoreboardTeam.setAllowFriendlyFire(false);
                scoreboardTeam.setPrefix("[" + s + "] ");
                scoreboardTeam.addPlayer(this.player);
            } else {
                player.board.getTeam(s).addPlayer(this.player);
            }
        }
    }

    public Player player() { return player; }
    public int kills() { return kills; }
    public void incrementKills() { ++kills; }
    public String team() { return team.name(); }
    public Collection<GamePlayer> teammates() { return team.players(); }

    @Override
    public int hashCode() {
        return player.hashCode();
    }
}
