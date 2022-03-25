package us.jcedeno.game.games.greenlight;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import us.jcedeno.game.games.GameManager;
import us.jcedeno.game.games.greenlight.enums.LightState;
import us.jcedeno.game.games.greenlight.listeners.GreenLightListener;
import us.jcedeno.game.games.greenlight.listeners.PreLightGameListener;
import us.jcedeno.game.games.greenlight.types.GLightData;
import us.jcedeno.game.games.greenlight.utils.tasks.PlayerArrayQueueShootTask;
import us.jcedeno.game.global.utils.Locations;
import us.jcedeno.game.global.utils.Sounds;
import us.jcedeno.game.players.PlayerManager;

public class GreenLightManager {

    private final @Getter GameManager gManager;
    private @Getter GreenLightGame greenLightGame;

    private @Getter Location cubeCenter;

    private Vector cubeCenter2D;

    private @Getter @Setter LightState lightState;

    private @Getter boolean isRunning;

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

        // TODO EXTRA FUNCTIONS.
    }

    public void runGame(int seconds) {
        if (this.isRunning)
            throw new IllegalStateException(
                    "The game " + greenLightGame.getClass().getSimpleName() + " is already running.");

        this.isRunning = true;

        gManager.getBukkitTimer().setTime(seconds);
        gManager.getBukkitTimer().start();

        greenLightGame.setTaskID(greenLightGame.runTaskTimer(gManager.getSquidInstance(), 0, 20).getTaskId());

        greenLightGame.greenLight(true);

        greenLightGame.setEndTaskID(Bukkit.getScheduler()
                .runTaskLater(gManager.getSquidInstance(), this::endGame, seconds * 20L).getTaskId());
    }

    public void endGame() {

       gManager.getBukkitTimer().end();

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

        gManager.getBukkitTimer().end();

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
        player.setHealth(0);
    }

    public void shootAll(boolean endGame) {
        ArrayList<Player> playerList = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (playerManager().isPlayer(p) && !playerManager().isDead(p)
                    && Locations.isInCube(cubeLower().clone().add(21, 0, 0), cubeUpper(), p.getLocation())) {
                playerList.add(p);
            }
        });
        new PlayerArrayQueueShootTask(this, playerList, endGame, 0, 40);
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public boolean inCube(Location location) {
        return Locations.isInCube(cubeLower(), cubeUpper(), location);
    }

    public void setCubeCenter2D(Location location) {
        this.cubeCenter = location;
        cubeCenter2D = new Vector(location.getX(), 0, location.getZ());
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
        return this.gManager.gData().gLightData();
    }

}
