package me.lofro.game.games.parkour;

import lombok.Getter;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.parkour.listeners.ParkourListener;
import me.lofro.game.games.parkour.types.ParkourData;
import me.lofro.game.global.utils.Locations;
import me.lofro.game.players.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ParkourManager {

    private final @Getter GameManager gManager;
    private final @Getter ParkourListener parkourListener;

    private final @Getter List<Player> winners = new ArrayList<>();

    private @Getter boolean isRunning = false;

    private @Getter int winnerLimit;

    public ParkourManager(GameManager gManager) {
        this.gManager = gManager;
        this.parkourListener = new ParkourListener(this);
    }

    public void runGame(int winnerLimit) {
        if (this.isRunning)
            throw new IllegalStateException(
                    "The game " + this.getClass().getSimpleName() + " is already running.");

        this.isRunning = true;
        this.winnerLimit = winnerLimit;

        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "sfx.lava_floor", 1f, 1f));

        gManager.getSquidInstance().registerListener(parkourListener);
    }

    public void endGame() {
        this.isRunning = false;

        winners.clear();

        gManager.getSquidInstance().unregisterListener(parkourListener);
        raiseLava();
    }

    public void stopGame() {
        this.isRunning = false;

        winners.clear();

        gManager.getSquidInstance().unregisterListener(parkourListener);
    }

    public void raiseLava() {
        var loc1 = parkourData().getAreaLower().clone();
        var loc2 = parkourData().getAreaUpper().clone();

        new BukkitRunnable() {
            int y = loc1.getBlockY();
            @Override
            public void run() {
                if (y >= loc2.getBlockY()) {
                    this.cancel();
                    return;
                }
                fillLava(loc1, loc2.clone().subtract(0, (loc2.clone().getY() - y), 0), true);
                y++;
            }
        }.runTaskTimer(gManager.getSquidInstance(), 0, 40);
    }

    public void descendLava() {
        fillLava(parkourData().getAreaLower(), parkourData().getAreaUpper(), false);
    }

    public void fillLava(Location loc1, Location loc2, boolean bool) {
        var locations = Locations.getBlocksInsideCube(loc1, loc2);

        locations.forEach(l -> {
            var block = l.getBlock();

            if (bool) {
                if (block.getType().equals(Material.AIR)) {
                    block.setType(Material.LAVA);
                }
            } else {
                if (block.getType().equals(Material.LAVA)) {
                    block.setType(Material.AIR);
                }
            }
        });
    }

    public void convertToWater() {
        var locations = Locations.getBlocksInsideCube(parkourData().getAreaLower(), parkourData().getAreaUpper());

        locations.forEach(l -> {
            var block = l.getBlock();

            if (block.getType().equals(Material.LAVA)) {
                block.setType(Material.WATER);
            }
        });
    }

    public void convertToLava() {
        var locations = Locations.getBlocksInsideCube(parkourData().getAreaLower(), parkourData().getAreaUpper());

        locations.forEach(l -> {
            var block = l.getBlock();

            if (block.getType().equals(Material.WATER)) {
                block.setType(Material.LAVA);
            }
        });
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public ParkourData parkourData() {
        return gManager.gameData().parkourData();
    }

    public boolean inGoal(Location location) {
        return Locations.isInCube(parkourData().getGoalLower(), parkourData().getGoalUpper(), location);
    }

}
