package me.rspacerr.paintball.commands;

import me.rspacerr.paintball.GameManager;
import org.bukkit.Bukkit;
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

        if (args.length != 3 && args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /changeteam <name> <player>");
            return false;
        }

        String name = args[1];
        if (args.length == 3) {
            // don't add if player is not online
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().equals(args[2])) {
                    GameManager.changeTeam(name, p);
                    sender.sendMessage(String.format("%sSuccessfully changed %s's team to %s.", ChatColor.GREEN, p.name(), name));
                    break;
                }
            }
        } else {
            Player p = (Player) sender;
            GameManager.changeTeam(name, p);
            sender.sendMessage(String.format("%sSuccessfully changed %s's team to %s.", ChatColor.GREEN, p.name(), name));
        }

        return true;
    }
}
