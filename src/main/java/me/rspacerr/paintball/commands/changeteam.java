package me.rspacerr.paintball.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class changeteam implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) {
            return false;
        }

        if (args.length > 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /changeteam Player RED/BLUE");
            return false;
        }

        if ( !(args[2].equalsIgnoreCase("RED") || args[2].equalsIgnoreCase("BLUE")) ) {
            sender.sendMessage(ChatColor.RED + "We currently do not support this team. Use team RED or BLUE");
            return false;
        }

        // TODO: add player to team
        return true;
    }
}
