package me.rspacerr.paintball;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Paintball extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.broadcastMessage(ChatColor.GREEN + "Paintballs loaded!");
    }

    @Override
    public void onDisable() {}
}
