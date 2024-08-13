package me.rspacerr.paintball;

import me.rspacerr.paintball.commands.*;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashSet;
import java.util.Set;

public final class PaintballPlugin extends JavaPlugin implements Listener {
    // plugin instance
    private static Plugin plugin;

    // managers
    public static ScoreboardManager manager = Bukkit.getScoreboardManager();
    public static Set<GamePlayer> players = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("changeteam").setExecutor(new changeteam());
        getCommand("setdamage").setExecutor(new setdamage());

        type typeCommand = new type();
        getCommand("type").setExecutor(typeCommand);
        getCommand("type").setTabCompleter(typeCommand);

        getCommand("start").setExecutor(new start());
        getCommand("end").setExecutor(new end());

        plugin = this;

        Bukkit.broadcastMessage(ChatColor.GREEN + "Paintball Plugin loaded!");
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + "Paintball plugin disabled!");

        if (GameManager.isActive()) {
            GameManager.end();
        }
    }

    public static Plugin plugin() { return plugin; }
}
