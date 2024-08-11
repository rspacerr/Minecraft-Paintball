package me.rspacerr.paintball.games;

import me.rspacerr.paintball.GameManager;
import me.rspacerr.paintball.GameUtil;
import me.rspacerr.paintball.players.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class Frostbite extends Game {
    private Set<GamePlayer> frozenPlayers;
    private final ItemStack ICE = new ItemStack(Material.ICE);

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @EventHandler
    public void onTag(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Arrow)) return;

        /* make arrows disappear on miss */
        if (e.getHitBlock() != null) {
            e.getEntity().remove();
            return;
        }

        if (e.getHitEntity() == null || !(e.getHitEntity() instanceof Player)) return;
        if (e.getEntity().getShooter() == null || !(e.getEntity().getShooter() instanceof Player)) return;

        Player hitPlayer = (Player) e.getHitEntity();
        GamePlayer hitGamePlayer = GameManager.getPlayer(hitPlayer);
        if (hitGamePlayer == null) {
            e.getEntity().remove();
            return;
        }

        Player shooter = (Player) e.getEntity().getShooter();
        GameUtil.hitSFX(shooter);

        hitPlayer.getInventory().setHelmet(ICE);
        frozenPlayers.add(hitGamePlayer);

        // check if full team is frozen

    }

    @Override
    public void death(GamePlayer player) {

    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        GamePlayer player = GameManager.getPlayer(e.getPlayer());
        if (player == null) return;

        /* prevent frozen players from moving */
        if (frozenPlayers.contains(player)) {
            e.setCancelled(true);
        }
    }
}
