package me.lofro.core.paper.listeners;

import lombok.Getter;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.games.GreenLightGame;
import me.lofro.core.paper.utils.location.Locations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;

public class GreenLightListener implements Listener {

    private final Main instance;
    private final Game game;

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

        Location cubePosition1 = greenLightGame.getCubeLocation1();
        Location cubePosition2 = greenLightGame.getCubeLocation2();

        if (!greenLightGame.isCancelled()) {
            if (game.isPlayer(player) && Locations.isInCube(cubePosition1, cubePosition2, location)) {
                movedList.add(player);
            }
        } else {
            if (game.isPlayer(player) && Locations.isInCube(cubePosition1.clone().add(21, 0, 0), cubePosition2, location)) {
                if (player.getLocation().getBlockY() != player.getLocation().getY()) return;
                e.setCancelled(true);
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

        if (!greenLightGame.isCancelled()) {
            if (game.isPlayer(player) && Locations.isInCube(cubePosition1, cubePosition2, location)) {
                movedList.add(player);
            }
        } else {
            if (game.isPlayer(player) && Locations.isInCube(cubePosition1.clone().add(21, 0, 0), cubePosition2, location)) {
                e.setCancelled(true);
            }
        }
    }

}
