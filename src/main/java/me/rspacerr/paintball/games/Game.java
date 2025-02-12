package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public abstract class Game implements Listener {
    protected List<GamePlayer> alivePlayers;
    protected int aliveTeams;


    public abstract void start();
    public abstract void end();

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
    void checkGameEnd() {
        if (aliveTeams <= 1) {
            GameManager.end();
        }
    }

    @EventHandler
    public void preventMovingOnStart(PlayerMoveEvent e) {
        GamePlayer player = GameManager.getPlayer(e.getPlayer());
        if (player == null) return;
        if (GameManager.starting) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        GamePlayer player = GameManager.getPlayer(e.getPlayer());
        if (player == null) return;

        e.setCancelled(true);
    }
}
