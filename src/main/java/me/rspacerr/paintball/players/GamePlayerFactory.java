package me.rspacerr.paintball.players;

import me.rspacerr.paintball.GameManager;
import org.bukkit.entity.Player;

public final class GamePlayerFactory {
    private GamePlayerFactory() {}

    public static GamePlayer create(Player player) {
        switch (GameManager.game()) {
            case PAINTBALL:
                return new PaintballPlayer(player);
            case FROSTBITE:
            case QUICKFIRE:
            default:
                return new GamePlayer(player);
        }
    }
}
