package us.jcedeno.game.games.greenlight.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import us.jcedeno.game.games.greenlight.GreenLightManager;
import us.jcedeno.game.games.greenlight.enums.LightState;

public class PreLightGameListener implements Listener {

    private final GreenLightManager gLightManager;

    private static final double repulsionVelocity = 1.5D;
    private static final Vector vector3Dto2D = new Vector(1, 0, 1);

    public PreLightGameListener(GreenLightManager gLightManager) {
        this.gLightManager = gLightManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!gLightManager.getLightState().equals(LightState.PRE_START)) return;

        var player = e.getPlayer();
        var location = player.getLocation();

        if (gLightManager.playerManager().isPlayer(player) && gLightManager.inCube(location)) {
            Vector opposite = player.getLocation().toVector().subtract(gLightManager.getCubeCenter2D());

            player.setVelocity(opposite.normalize().multiply(vector3Dto2D).multiply(repulsionVelocity));
        }
    }
    
}
