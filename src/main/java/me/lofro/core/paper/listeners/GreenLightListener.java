package me.lofro.core.paper.listeners;

import lombok.Getter;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.games.GreenLightGame;
import me.lofro.core.paper.utils.location.Locations;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class GreenLightListener implements Listener {

    private final Main instance;
    private final Game game;

    private static final double repulsionVelocity = 1.5D;
    private static final Vector vector3Dto2D = new Vector(1, 0, 1);

    private final @Getter ArrayList<Player> movedList = new ArrayList<>();

    public GreenLightListener(Main instance, Game game) {
        this.instance = instance;
        this.game = game;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location location = player.getLocation();

        GreenLightGame greenLightGame = game.getGreenLightGame();

        if (game.isPlayer(player) && Locations.isInCube(greenLightGame.getCubeLocation1(), greenLightGame.getCubeLocation2(), location)) {
            if (greenLightGame.getLightState() == GreenLightGame.LightState.PRE_START) {
                Vector opposite = player.getLocation().toVector().subtract(greenLightGame.getCubeCenter2D());

                player.setVelocity(opposite.normalize().multiply(vector3Dto2D).multiply(repulsionVelocity));
            } else {
                if (greenLightGame.isRunning()) {
                    if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR) || game.isDead(player)) return;
                    movedList.add(player);
                } else {
                    if (player.getLocation().getBlockY() != player.getLocation().getY()) return;
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        Location location = player.getLocation();

        GreenLightGame greenLightGame = game.getGreenLightGame();

        Location cubePosition1 = greenLightGame.getCubeLocation1();
        Location cubePosition2 = greenLightGame.getCubeLocation2();

        if (!greenLightGame.isRunning()) return;

        if (game.isPlayer(player) && Locations.isInCube(cubePosition1, cubePosition2, location)) {
            movedList.add(player);
        }
    }

}
