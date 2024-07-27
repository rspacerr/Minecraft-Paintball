package me.rspacerr.paintball.games;

import me.rspacerr.paintball.PaintballPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class Paintball {
    // gameplay
    private Map<UUID, PaintballPlayer> players;

    // variables
    public static double damage = 2;


    @EventHandler
    public void onShoot(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) { return; }

        Player player = e.getPlayer();
        if (player.getGameMode().equals(GameMode.SPECTATOR)) return;

        /* shoot paintballs */
        if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HORSE_ARMOR) {
            Snowball proj = player.launchProjectile(Snowball.class);
            proj.setVelocity(proj.getVelocity().multiply(1.25));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 2);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball)) return;

        if (e.getHitEntity() == null) return;
        if ( !(e.getHitEntity() instanceof Player) ) return;

        Player hitPlayer = (Player) e.getHitEntity();
        Player shooter = (Player) e.getEntity().getShooter();
        Vector paintballVelocity = e.getEntity().getVelocity();

        shooter.playSound(shooter.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 5);
        if (hitPlayer.getHealth() - damage <= 0) {
            hitPlayer.setGameMode(GameMode.SPECTATOR);
            hitPlayer.setHealth(20);
            // TODO: broadcast death message
        } else {
            hitPlayer.damage(damage);
            hitPlayer.setVelocity(new Vector(paintballVelocity.getX()*0.1, 0.5, paintballVelocity.getZ()*0.1));
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();

        if (players.containsKey(id)) {
            players.remove(id);
        }
    }
}
