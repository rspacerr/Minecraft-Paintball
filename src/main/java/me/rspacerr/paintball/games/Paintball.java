package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameUtil;
import me.rspacerr.paintball.PaintballPlugin;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class Paintball extends Game {
    // variables
    public static double damage = 2;
    public static final long COOLDOWN_TIME = 1000; // 1 second
    Map<UUID, Long> cooldowns = new HashMap<>();
    Map<UUID, Boolean> canShoot = new HashMap<>();

    int cooldownTaskID = -1;

    @Override
    public void start() {
        for (GamePlayer pl : GameManager.players()) {
            cooldowns.put(pl.player().getUniqueId(), System.currentTimeMillis());
            canShoot.put(pl.player().getUniqueId(), true);
        }

        cooldownTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(PaintballPlugin.plugin(), () -> {
            Iterator<Map.Entry<UUID, Long>> iterator = cooldowns.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<UUID, Long> entry = iterator.next();
                long storedTime = entry.getValue();

                if (System.currentTimeMillis() - storedTime >= COOLDOWN_TIME) {
                    canShoot.put(entry.getKey(), true);
                    iterator.remove();
                }
            }
        }, 20, 1);
    }

    @EventHandler
    public void onShoot(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) { return; }

        Player player = e.getPlayer();
        if (player.getGameMode().equals(GameMode.SPECTATOR)) return;

        /* shoot paintballs */
        if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HORSE_ARMOR) {

            if (canShoot.containsKey(player.getUniqueId()) && canShoot.get(player.getUniqueId())) {
                Snowball proj = player.launchProjectile(Snowball.class);
                proj.setVelocity(proj.getVelocity().multiply(1.25));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 2);

                canShoot.put(player.getUniqueId(), false);
                cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getHitEntity() == null) return;
        if (!(e.getEntity() instanceof Snowball)) return;
        if (!(e.getHitEntity() instanceof Player hitPlayer)) return;
        if (!(e.getEntity().getShooter() instanceof Player shooter)) return;

        // Check if same team
        GamePlayer hitGamePlayer = GameManager.getPlayer(hitPlayer);
        GamePlayer shooterGamePlayer = GameManager.getPlayer(shooter);
        if (hitGamePlayer.team().equals(shooterGamePlayer.team())) return;

        Projectile projectile = e.getEntity();

        Vector paintballVelocity = projectile.getVelocity();

        GameUtil.hitSFX(shooter);
        if (hitPlayer.getHealth() - damage <= 0) {
            hitPlayer.setGameMode(GameMode.SPECTATOR);
            hitPlayer.setHealth(20);
            // TODO: broadcast death message
        } else {
            hitPlayer.damage(damage);
            hitPlayer.setVelocity(new Vector(paintballVelocity.getX()*0.1, 0.15, paintballVelocity.getZ()*0.1));
        }
        projectile.remove();
    }

    @Override
    public void death(GamePlayer player) {

    }

    @Override
    public void end() {
        Bukkit.getScheduler().cancelTask(cooldownTaskID);
    }

    /* as default behavior, remove players that disconnect. TODO: check for team death */
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        GameManager.removePlayer(e.getPlayer());
    }
}
