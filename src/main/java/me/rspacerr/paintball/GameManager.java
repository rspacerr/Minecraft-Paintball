package me.rspacerr.paintball;

import me.rspacerr.paintball.games.*;
import me.rspacerr.paintball.players.GamePlayer;
import me.rspacerr.paintball.players.GamePlayerFactory;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.*;

public final class GameManager {
    // Functionality
    private static Map<UUID, GamePlayer> players = new HashMap<>();
    private static Map<String, GameTeam> teams = new HashMap<>();
    private static GameType type = GameType.PAINTBALL;
    private static Game game = null;

    // settings
    public static boolean ALLOW_PUNCHING = false;

    private GameManager() {}

    public static boolean startGame() {
        if (game != null) return false;

        switch (type) {
            case PAINTBALL:
                game = new Paintball();
                break;
            case QUICKFIRE:
                game = new Quickfire();
                break;
            case FROSTBITE:
                game = new Frostbite();
                break;
            default:
                return false;
        };

        PaintballPlugin.plugin().getServer().getPluginManager().registerEvents(game, PaintballPlugin.plugin());
        game.start();
        return true;
    }


    /**
     * Returns the GamePlayer associated with the given player.
     * @param player Player whose GamePlayer representation to return.
     * @return The GamePlayer representation of `player`
     */
    public static GamePlayer getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    /**
     * Creates a new GamePlayer representation of player and adds them to the game roster
     * @param player adds player to game roster
     * @return GamePlayer representation of the added player
     */
    public static GamePlayer addPlayer(Player player) {
        GamePlayer newPlayer = GamePlayerFactory.create(player);
        players.put(player.getUniqueId(), newPlayer);
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

    /**
     * Returns team associated with given name
     * @param name Name of the team
     * @return the team assigned the name.
     */
    public static GameTeam getTeam(String name) {
        return teams.get(name);
    }

    /**
     * Returns true if player's team is entirely dead.
     * @param player GamePlayer that has just died
     * @return true if the entire team is dead, false otherwise
     */
    public static boolean isTeamDead(GamePlayer player) {
        int deadTeammates = 0;
        for (GamePlayer p : player.teammates()) {
            deadTeammates += p.player().getGameMode() == GameMode.SPECTATOR ? 1 : 0;
        }
        return deadTeammates == teams.get(player.team()).players().size();
    }

    /* For iterating through all players */
    public static Collection<GamePlayer> players() {
        return Collections.unmodifiableCollection(players.values());
    }

    /* For iterating through all teams */
    public static Collection<GameTeam> teams() {
        return Collections.unmodifiableCollection(teams.values());
    }

    /* determine if a game is currently happening */
    public static boolean isActive() {
        return game != null;
    }

    /* get type of game; prevent rep exposure by returning game */
    public static GameType game() {
        return type;
    }

    /* end game on shutdown or other critical failure */
    public static void end() {
        game.end();
    }

    public static void setGameType(GameType type) {
        GameManager.type = type;
    }
}
