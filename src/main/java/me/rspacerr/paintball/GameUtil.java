package me.rspacerr.paintball;

import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;


public final class GameUtil {

    public static void hitSFX(Player player) {
        player.playSound(player, Sound.ENTITY_ARROW_HIT_PLAYER, 1, 3);
    }

    public static void spawnFirework(GamePlayer player) {
        Location l = player.player().getLocation();
        l.setY(l.getY()+1);
        org.bukkit.entity.Firework fw = (org.bukkit.entity.Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    private GameUtil() {}
}
