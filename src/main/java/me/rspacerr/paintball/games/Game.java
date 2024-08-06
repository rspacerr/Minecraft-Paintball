package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameTeam;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class Game implements Listener {
    protected List<GamePlayer> alivePlayers;
    protected List<GameTeam> aliveTeams;


    public abstract void start();
    public abstract void end();

    /**
     * Handles death effect graphics and logic.
     * @implSpec calls checkGameEnd();
     * @param player Player that died.
     */
    public abstract void death(GamePlayer player);

    /**
     * Trigger end game events if there is only one team alive.
     */
    private void checkGameEnd() {
        if (aliveTeams.size() == 1) {
            end();
        }
    }
}
