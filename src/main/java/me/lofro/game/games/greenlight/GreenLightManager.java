package me.lofro.game.games.greenlight;

import java.util.ArrayList;
import java.util.List;

import com.destroystokyo.paper.ParticleBuilder;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.greenlight.listeners.PreLightGameListener;
import me.lofro.game.games.greenlight.types.GLightData;
import me.lofro.game.games.greenlight.utils.tasks.PlayerArrayQueueShootTask;
import me.lofro.game.global.utils.LineVector;
import me.lofro.game.global.utils.Locations;
import me.lofro.game.global.utils.Sounds;
import me.lofro.game.players.PlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import me.lofro.game.games.greenlight.enums.LightState;
import me.lofro.game.games.greenlight.listeners.GreenLightListener;

public class GreenLightManager {

    private final @Getter GameManager gManager;
    private @Getter GreenLightGame greenLightGame;

    private @Getter Location cubeCenter;

    private Vector cubeCenter2D;

    private @Getter @Setter LightState lightState;

    private @Getter boolean isRunning = false;

    private final @Getter PreLightGameListener preGameListener;
    private final @Getter GreenLightListener greenLightListener;

    private @Getter @Setter int greenLowestTimeBound = 3;
    private @Getter @Setter int greenHighestTimeBound = 10;

    private @Getter @Setter int redLowestTimeBound = 10;
    private @Getter @Setter int redHighestTimeBound = 20;

    private @Getter final World world;

    public GreenLightManager(GameManager gManager, World world) {
        this.gManager = gManager;

        this.preGameListener = new PreLightGameListener(this);
        this.greenLightListener = new GreenLightListener(this);

        this.world = world;
        this.cubeCenter = Locations.getCubeCenter(world, cubeLower(), cubeUpper());
        setCubeCenter2D(cubeCenter);

        this.greenLightGame = new GreenLightGame(this);
    }

    public void preStart() {
        this.lightState = LightState.PRE_START;

        gManager.getSquidInstance().registerListener(preGameListener);

        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "sfx.green_red_light", 1, 1));
    }

    public void stopPreStart() {
        this.lightState = null;

        gManager.getSquidInstance().unregisterListener(preGameListener);
    }

    public void runGame(int seconds) {
        if (this.isRunning)
            throw new IllegalStateException(
                    "The game " + greenLightGame.getClass().getSimpleName() + " is already running.");

        this.isRunning = true;

        gManager.getTimer().start(seconds);

        gManager.getSquidInstance().unregisterListener(preGameListener);

        greenLightGame.setTaskID(greenLightGame.runTaskTimer(gManager.getSquidInstance(), 0, 20).getTaskId());

        greenLightGame.greenLight(true);

        greenLightGame.setEndTaskID(Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::endGame, seconds * 20L).getTaskId());
    }

    public void endGame() {

        greenLightGame.cancel();
        this.isRunning = false;

        if (this.lightState.equals(LightState.GREEN_LIGHT))
            gManager.getSquidInstance().registerListener(greenLightListener);

        greenLightGame.setShootAllTaskID(Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), () -> shootAll(true), 20 * 10).getTaskId());

        this.greenLightGame = new GreenLightGame(this);
    }

    public void stopGame() {
        Bukkit.getScheduler().cancelTask(greenLightGame.getEndTaskID());
        Bukkit.getScheduler().cancelTask(greenLightGame.getShootAllTaskID());

        gManager.getTimer().end();

        greenLightGame.cancel();
        this.isRunning = false;

        gManager.getSquidInstance().unregisterListener(greenLightListener);

        this.greenLightGame = new GreenLightGame(this);
    }

    public void shoot(Player player) {
        if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE)
                || playerManager().isDead(player))
            return;
        Sounds.playSoundDistance(cubeCenter, 150, "sfx.dramatic_gun_shots", 1f, 1f);
        if (!cannonLocations().isEmpty()) shootCannon(player, 0.25);
        player.setHealth(0);
    }

    public void shootAll(boolean endGame) {
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (playerManager().isGuard(p)) continue;
            if (playerManager().isPlayer(p) && !playerManager().isDead(p)
                    && Locations.isInCube(cubeLower(), cubeUpper(), p.getLocation())) {
                playerList.add(p);
            }
        }
        new PlayerArrayQueueShootTask(this, playerList, endGame, 0, 40);
    }

    double taxiDistance(Location a, Location b) {
        return (Math.abs(a.getX() - b.getX()) + Math.abs(a.getZ() - b.getZ()));
    }

    private Location closestCannon(Location loc) {
        var cannonIter = gManager.gData().greenLightData().getCannonLocations().iterator();
        var greatestDistance = Double.MAX_VALUE;
        Location closestCannon = null;

        while (cannonIter.hasNext()) {
            var next = cannonIter.next();
            var d = taxiDistance(next, loc);
            if (d < greatestDistance) {
                greatestDistance = d;
                closestCannon = next;
            }
        }
        return closestCannon;
    }

    private void shootCannon(Player player, double t) {
        var cannon = closestCannon(player.getLocation());
        var points = LineVector.of(player.getLocation().add(0, player.getEyeHeight(), 0).toVector(),cannon.clone().add(0.5, 0.5, 0.5).toVector()).getPointsInBetween(t);

        points.forEach(p -> new ParticleBuilder(Particle.REDSTONE)
                .color(Color.RED)
                .location(p.toLocation(world))
                .force(true)
                .count(15)
                .offset(0.000001, 0.000001, 0.000001)
                .extra(0)
                .spawn());
    }

    public void setCubeCenter2D(Location location) {
        this.cubeCenter = location;
        cubeCenter2D = new Vector(location.getX(), 0, location.getZ());
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public boolean inCube(Location location) {
        return Locations.isInCube(cubeLower(), cubeUpper(), location);
    }

    public Vector getCubeCenter2D() {
        return cubeCenter2D.clone();
    }

    public Location cubeUpper() {
        return gLightData().getCubeUpper();
    }

    public Location cubeLower() {
        return gLightData().getCubeLower();
    }

    public List<Location> cannonLocations() {
        return gLightData().getCannonLocations();
    }

    private GLightData gLightData() {
        return this.gManager.gData().greenLightData();
    }

}
