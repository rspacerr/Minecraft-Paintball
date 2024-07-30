package me.rspacerr.paintball;

import me.rspacerr.paintball.commands.changeteam;
import me.rspacerr.paintball.commands.setdamage;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashSet;
import java.util.Set;

public final class PaintballPlugin extends JavaPlugin implements Listener {
    // managers
    public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Set<GamePlayer> players = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        setupCommands();

        Bukkit.broadcastMessage(ChatColor.GREEN + "Paintball Plugin loaded!");
    }

    private void setupCommands() {
        getCommand("changeteam").setExecutor(new changeteam());
        getCommand("setdamage").setExecutor(new setdamage());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        GameManager.removePlayer(e.getPlayer());
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + "Paintball plugin disabled!");
    }
}
