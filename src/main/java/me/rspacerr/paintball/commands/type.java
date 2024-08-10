package me.rspacerr.paintball.commands;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.games.GameType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class type implements CommandExecutor, TabCompleter {
    public static List<String> types = List.of("paintball", "quickfire", "frostbite");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) {
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /type <type>");
            return false;
        }


        String mode = args[1].toLowerCase();
        switch (mode) {
            case "paintball":
                GameManager.setGameType(GameType.PAINTBALL);
                break;
            case "quickfire":
                GameManager.setGameType(GameType.QUICKFIRE);
                break;
            case "frostbite":
                GameManager.setGameType(GameType.FROSTBITE);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid type specified!");
                return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Successfully updated game type!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return types;
    }
}
