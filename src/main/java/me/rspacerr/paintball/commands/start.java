package me.rspacerr.paintball.commands;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameTeam;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) {
            return false;
        }

        if (GameManager.teams().size() < 2) {
            Bukkit.broadcastMessage(ChatColor.RED + "Failed to start game!");
            for (GameTeam gt : GameManager.teams()) {
                Bukkit.broadcastMessage("Team " + gt.name() + " has members: ");
                for (GamePlayer pl : gt.players()) {
                    Bukkit.broadcastMessage(pl.player().toString());
                }
            }
            return false;
        }

        return GameManager.startGame();
    }
}