package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameUtil;
import me.rspacerr.paintball.players.GamePlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class Quickfire extends Game {
    private final int DAMAGE = 2;
    private int health = 8;

    @Override
    public void start() {

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getHitEntity() == null) return;
        if (!(e.getEntity() instanceof Arrow)) return;
        if (!(e.getHitEntity() instanceof Player hitPlayer)) return;
        if (!(e.getEntity().getShooter() instanceof Player shooter)) return;

        // Check if same team
        GamePlayer hitGamePlayer = GameManager.getPlayer(hitPlayer);
        GamePlayer shooterGamePlayer = GameManager.getPlayer(shooter);
        if (hitGamePlayer.team().equals(shooterGamePlayer.team())) return;

        Projectile projectile = e.getEntity();

        shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hitPlayer.getName() + " - " + ChatColor.RED + ((int)(hitPlayer.getHealth()-2))/2+ " ♥"));
        GameUtil.hitSFX(shooter);

        if (hitPlayer.getHealth() <= DAMAGE) {
            // TODO: better death?
            death(GameManager.getPlayer(hitPlayer));
        } else {
            hitPlayer.damage(DAMAGE);
            hitPlayer.setVelocity(new Vector(projectile.getVelocity().getX()*0.15, 0.3, projectile.getVelocity().getZ()*0.15));
        }
        projectile.remove();
    }

    @Override
    public void death(GamePlayer player) {

    }

    @Override
    public void end() {

    }
}
