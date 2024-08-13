package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameUtil;
import me.rspacerr.paintball.PaintballPlugin;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Set;

public class Frostbite extends Game {
    private Set<GamePlayer> frozenPlayers = new HashSet<>();
    private final ItemStack ICE = new ItemStack(Material.ICE);

    // (given) items
    private final ItemStack BOW = new ItemStack(Material.BOW);
    private final ItemStack ARROW = new ItemStack(Material.ARROW);

    @Override
    public void start() {
        aliveTeams = GameManager.teams().size();
        initializeItems();

        for (GamePlayer player : GameManager.players()) {
            Player p = player.player();
            p.getInventory().clear();
            p.setMaxHealth(20);
            p.setHealth(20);
            p.getInventory().addItem(BOW);
            p.getInventory().addItem(ARROW);
            for (GamePlayer other : GameManager.players()) {
                if (other.player().getUniqueId() == player.player().getUniqueId()) continue;
                if (player.board.getTeam(other.team()) == null) continue;
                player.board.getTeam(other.team()).setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            }
            p.setGameMode(GameMode.ADVENTURE);
        }

        GameManager.countdown();
    }

    @Override
    public void end() {
        HandlerList.unregisterAll(this);
        frozenPlayers.clear();

        for (GamePlayer player : GameManager.players()) {
            if (player.player().getInventory().getItem(39) == null) continue;
            if (player.player().getInventory().getItem(39).getType() == Material.ICE) {
                player.player().getInventory().setHelmet(null);
            }
            for (GamePlayer other : GameManager.players()) {
                if (other.player().getUniqueId() == player.player().getUniqueId()) continue;
                if (player.board.getTeam(other.team()) == null) continue;
                player.board.getTeam(other.team()).setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            }
        }
    }

    @EventHandler
    public void onTag(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;

        /* make arrows disappear on miss */
        if (e.getHitBlock() != null) {
            e.getEntity().remove();
            return;
        }

        if (e.getHitEntity() == null || !(e.getHitEntity() instanceof Player)) return;
        if (e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Player)) return;

        Player hitPlayer = (Player) e.getHitEntity();
        GamePlayer hitGamePlayer = GameManager.getPlayer(hitPlayer);
        if (hitGamePlayer == null) {
            e.getEntity().remove();
            return;
        }

        Player shooter = (Player) e.getEntity().getShooter();
        GamePlayer shooterGamePlayer = GameManager.getPlayer(shooter);

        if (shooterGamePlayer == null) {
            e.getEntity().remove();
            return;
        }

        if (hitGamePlayer.team().equals(shooterGamePlayer.team())) {
            e.setCancelled(true);
            e.getEntity().remove();
            return;
        }

        GameUtil.hitSFX(shooter);

        e.setCancelled(true);
        hitPlayer.damage(0.5);
        hitPlayer.sendTitle(" ", ChatColor.AQUA + "Frozen!", 0, 60, 30);
        hitPlayer.getInventory().clear();

        // announce shot to both teams
        for (GamePlayer player : shooterGamePlayer.teammates()) {
            player.player().sendMessage(String.format("%s was frozen by %s", hitPlayer.getName(), shooter.getName()));
        }

        for (GamePlayer player : hitGamePlayer.teammates()) {
            player.player().sendMessage(String.format("%s was frozen by %s", hitPlayer.getName(), shooter.getName()));
        }

        hitPlayer.getInventory().setHelmet(ICE);
        frozenPlayers.add(hitGamePlayer);

        Bukkit.getScheduler().scheduleSyncDelayedTask(PaintballPlugin.plugin(), new Runnable() {
            @Override
            public void run() {
                int deadTeammates = 0;
                for (GamePlayer player : hitGamePlayer.teammates()) {
                    if (frozenPlayers.contains(player)) {
                        deadTeammates++;
                    }
                }
                if (deadTeammates == hitGamePlayer.teammates().size()) {
                    aliveTeams--;
                    Bukkit.broadcastMessage("Team " + hitGamePlayer.team() + " was eliminated!");
                    for (GamePlayer player : hitGamePlayer.teammates()) {
                        player.player().setGameMode(GameMode.SPECTATOR);
                    }
                    checkGameEnd();
                }
            }
        }, 60);
    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent e) {
        if (! (e.getEntity() instanceof Player && e.getDamager() instanceof Player)) return;

        GamePlayer player = GameManager.getPlayer((Player) e.getEntity());
        GamePlayer damager = GameManager.getPlayer((Player) e.getDamager());

        if (player == null || damager == null) return;

        if (player.team().equals(damager.team()) && frozenPlayers.contains(player) && !frozenPlayers.contains(damager)) {
            e.setCancelled(true);
            frozenPlayers.remove(player);
            player.player().getInventory().setHelmet(null);
            player.player().getInventory().addItem(BOW);
            player.player().getInventory().addItem(ARROW);
            return;
        }

        if (player.team().equals(damager.team()) || !(player.team().equals(damager.team()) && !GameManager.ALLOW_PUNCHING)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        GamePlayer player = GameManager.getPlayer(e.getPlayer());
        if (player == null) return;

        /* prevent frozen players from moving */
        if (frozenPlayers.contains(player)) {
            e.setCancelled(true);
        }

        /* don't move at the beginning */
        if (GameManager.starting) {
            e.setCancelled(true);
        }
    }

    /* load default item settings */
    private void initializeItems() {
        ItemMeta meta = BOW.getItemMeta();
        meta.setUnbreakable(true);
        BOW.setItemMeta(meta);
        BOW.addEnchantment(Enchantment.ARROW_INFINITE, 1);
    }
}
