package me.rspacerr.paintball.commands;

import me.rspacerr.paintball.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class end implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) {
            return false;
        }


        if (!(GameManager.isActive())) {
           sender.sendMessage(ChatColor.RED + "Failed to end game! There is no game currently active!");
           return false;
        }

        GameManager.end();
        return true;
    }
}
