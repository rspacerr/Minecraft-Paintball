package me.rspacerr.paintball;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class GameUtil {
    public static Sound HIT = Sound.ENTITY_ARROW_HIT_PLAYER;


    public static void hitSFX(Player player) {
        player.playSound(player, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
    }

    private GameUtil() {}
}
