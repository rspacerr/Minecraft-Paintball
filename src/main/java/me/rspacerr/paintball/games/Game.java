package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameTeam;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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


    /* prevent punching */
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent e) {
        if (! (e.getEntity() instanceof Player && e.getDamager() instanceof Player)) return;

        if ( !GameManager.ALLOW_PUNCHING ) {
            e.setCancelled(true);
        }
    }

    /**
     * Trigger end game events if there is only one team alive.
     */
    private void checkGameEnd() {
        if (aliveTeams.size() == 1) {
            end();
        }
    }
}
