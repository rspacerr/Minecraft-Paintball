package me.rspacerr.paintball;

import me.rspacerr.paintball.commands.changeteam;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public final class Paintball extends JavaPlugin implements Listener {
    private Map<UUID, PaintballPlayer> players;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        setupCommands();

        Bukkit.broadcastMessage(ChatColor.GREEN + "Paintball Plugin loaded!");
    }

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
    public void onDisconnect(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();

        if (players.containsKey(id)) {
            players.remove(id);
            // TODO: Game Logic, i.e. remove from team
        }
    }

    private void setupCommands() {
        getCommand("changeteam").setExecutor(new changeteam());
    }

    @Override
    public void onDisable() {}
}
