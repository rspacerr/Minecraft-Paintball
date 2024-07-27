package me.rspacerr.paintball;

import me.rspacerr.paintball.commands.changeteam;
import me.rspacerr.paintball.commands.setdamage;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

public final class PaintballPlugin extends JavaPlugin implements Listener {
    // managers
    public static ScoreboardManager manager = Bukkit.getScoreboardManager();

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

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.RED + "Paintball plugin disabled!");
    }
}
