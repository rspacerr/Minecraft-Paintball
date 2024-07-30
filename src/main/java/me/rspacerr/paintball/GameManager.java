package me.rspacerr.paintball;

import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {
    private static Map<UUID, GamePlayer> players = new HashMap<>();
    private static Map<String, GameTeam> teams = new HashMap<>();

    private GameManager() {}

    /**
     * Returns the GamePlayer associated with the given player.
     * @param player Player whose GamePlayer representation to return.
     * @return The GamePlayer representation of `player`
     */
    public static GamePlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public static GamePlayer addPlayer(Player player) {
        GamePlayer newPlayer = new GamePlayer(player);
        players.put(player.getUniqueId(), new GamePlayer(player));
        return newPlayer;
    }

    public static GamePlayer removePlayer(Player player) {
        return players.remove(player.getUniqueId());
    }
    /**
     * Changes the team of a player. Makes a new team if team does not exist.
     * @param teamName The name of the team to change.
     * @param player The player who will be joining a new team.
     */
    public static void changeTeam(String teamName, Player player) {
        if ( !(teams.containsKey(teamName)) ) {
            addTeam(teamName);
        }

        // check if player is on a team
        if ( !(players.containsKey(player.getUniqueId())) ) {
            addPlayer(player).changeTeam(teamName);
        } else {
            players.get(player.getUniqueId()).changeTeam(teamName);
        }
    }

    /**
     * Adds a new team with the given name to the game context
     * @param name Name of the new team
     */
    public static void addTeam(String name) {
        teams.put(name, new GameTeam(name));
    }


    /* For iterating through all players */
    public static Collection<GamePlayer> players() {
        return Collections.unmodifiableCollection(players.values());
    }

    /* For iterating through all teams */
    public static Collection<GameTeam> teams() {
        return Collections.unmodifiableCollection(teams.values());
    }
}
