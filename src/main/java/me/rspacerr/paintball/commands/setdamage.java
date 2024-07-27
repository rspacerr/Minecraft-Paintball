package me.rspacerr.paintball.commands;

import me.rspacerr.paintball.games.Paintball;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class setdamage implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if ( !(sender instanceof Player)) {
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setdamage [number]");
            return false;
        }

        try {
            Paintball.damage = Double.parseDouble(args[1]);
        } catch(NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Usage: /setdamage [number]");
            return false;
        }

        return true;
    }
}
