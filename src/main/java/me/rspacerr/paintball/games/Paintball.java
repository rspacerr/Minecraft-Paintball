package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.players.PaintballPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Paintball extends Game {
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
        if (e.getHitEntity() == null) return;
        if (!(e.getHitEntity() instanceof Player hitPlayer)) return;
        if (!(e.getEntity().getShooter() instanceof Player shooter)) return;

        Projectile proj = e.getEntity();

        if (e.getEntity() instanceof Snowball) {
            paintballHit(hitPlayer, shooter, proj);
            return;
        }

        // TODO: add freezing for melt mode
        if (e.getEntity() instanceof Arrow) {
            // TODO: check GAME_TYPE

            quickfireHit(hitPlayer, shooter, proj);
            return;
        }

    }

    private void paintballHit(Player hitPlayer, Player shooter, Projectile projectile) {
        Vector paintballVelocity = projectile.getVelocity();

        shooter.playSound(shooter.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 5);
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

    private void quickfireHit(Player hitPlayer, Player shooter, Projectile projectile) {
        shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hitPlayer.getName() + " - " + ChatColor.RED + ((int)(hitPlayer.getHealth()-2))/2+ " ♥"));
        shooter.playSound(shooter, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);

        if (hitPlayer.getHealth() <= 2) {
            // TODO: better death?
            hitPlayer.setGameMode(GameMode.SPECTATOR);
        } else {
            hitPlayer.damage(2);
            hitPlayer.setVelocity(new Vector(projectile.getVelocity().getX()*0.15, 0.3, projectile.getVelocity().getZ()*0.15));
        }
        projectile.remove();
    }



    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        GameManager.removePlayer(e.getPlayer());
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }
}
