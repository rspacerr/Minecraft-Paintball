package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameUtil;
import me.rspacerr.paintball.PaintballPlugin;
import me.rspacerr.paintball.players.GamePlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class Quickfire extends Game {
    private final int DAMAGE = 2;
    private int health = 8;

    // items
    private final ItemStack CROSSBOW = new ItemStack(Material.CROSSBOW);
    private final ItemStack ARROWS = new ItemStack(Material.ARROW, 64);
    private final ItemStack ARROW = new ItemStack(Material.ARROW);

    @Override
    public void start() {
        initializeItems();
        for (GamePlayer pl : GameManager.players()) {
            Player player = pl.player();
            player.getInventory().clear();
            player.getInventory().addItem(CROSSBOW);
            player.getInventory().addItem(ARROWS);
            player.setMaxHealth(health);
            player.setHealth(health);
            player.getWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);
            for (GamePlayer other : GameManager.players()) {
                if (other.player().getUniqueId() == pl.player().getUniqueId()) continue;
                if (pl.board.getTeam(other.team()) == null) continue;
                pl.board.getTeam(other.team()).setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            }
        }
        GameManager.countdown();
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;
        if (!(e.getEntity().getShooter() instanceof Player shooter)) return;
        if (e.getHitEntity() == null) {
            e.getEntity().remove();
            return;
        }
        if (!(e.getHitEntity() instanceof Player hitPlayer)) return;

        // Check if same team
        GamePlayer hitGamePlayer = GameManager.getPlayer(hitPlayer);
        GamePlayer shooterGamePlayer = GameManager.getPlayer(shooter);
        if (hitGamePlayer.team().equals(shooterGamePlayer.team())) {
            e.setCancelled(true);
            e.getEntity().remove();
            return;
        }

        Projectile projectile = e.getEntity();

        shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(hitPlayer.getName() + " - " + ChatColor.RED + ((int)(hitPlayer.getHealth()-2))/2+ " ♥"));
        GameUtil.hitSFX(shooter);
        e.setCancelled(true);

        if (hitPlayer.getHealth() <= DAMAGE) {
            death(GameManager.getPlayer(hitPlayer), shooterGamePlayer);
        } else {
            hitPlayer.damage(DAMAGE);
            hitPlayer.setVelocity(new Vector(projectile.getVelocity().getX()*0.15, 0.3, projectile.getVelocity().getZ()*0.15));
        }
        projectile.remove();
    }

    public void death(GamePlayer victim, GamePlayer shooter) {
        Bukkit.broadcastMessage(victim.player().getName() + " was shot by " + shooter.player().getName());
        GameUtil.spawnFirework(victim);
        victim.player().setGameMode(GameMode.SPECTATOR);

        if (GameManager.isTeamDead(victim)) {
            Bukkit.broadcastMessage("Team " + victim.team() + " was eliminated!");
        }
        aliveTeams--;
        checkGameEnd();
    }

    @Override
    public void end() {
        HandlerList.unregisterAll(this);
        for (GamePlayer pl : GameManager.players()) {
            Player player = pl.player();
            player.setMaxHealth(20);
            player.setHealth(20);
            player.getWorld().setGameRule(GameRule.NATURAL_REGENERATION, true);
            for (GamePlayer other : GameManager.players()) {
                if (other.player().getUniqueId() == pl.player().getUniqueId()) continue;
                if (pl.board.getTeam(other.team()) == null) continue;
                pl.board.getTeam(other.team()).setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            }
        }
    }

    /* prevent firework damage */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;
        if (!(e.getEntity().getShooter() instanceof Player)) return;

        Player shooter = (Player) e.getEntity().getShooter();
        if (GameManager.getPlayer(shooter) == null) return;
        shooter.getInventory().addItem(ARROW);
    }


    /* defaults for items */
    private void initializeItems() {
        ItemMeta meta = CROSSBOW.getItemMeta();
        meta.setUnbreakable(true);
        CROSSBOW.setItemMeta(meta);
        CROSSBOW.addEnchantment(Enchantment.QUICK_CHARGE, 3);
    }
}
