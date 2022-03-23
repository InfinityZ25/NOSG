package us.jcedeno.game.games.greenlight.listeners;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import us.jcedeno.game.games.greenlight.GreenLightManager;

import java.util.ArrayList;

public class GreenLightListener implements Listener {

    private final GreenLightManager gLightManager;

    private final @Getter ArrayList<Player> movedList = new ArrayList<>();

    public GreenLightListener(GreenLightManager gLightManager) {
        this.gLightManager = gLightManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        var location = player.getLocation();

        if (gLightManager.playerManager().isPlayer(player) && gLightManager.inCube(location)) {
            if (gLightManager.isRunning()) {
                if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR) || gLightManager.playerManager().isDead(player)) return;
                movedList.add(player);
            } else {
                if (player.getLocation().getBlockY() != player.getLocation().getY()) return;
                e.setCancelled(true);
            }
        }
    }

}
