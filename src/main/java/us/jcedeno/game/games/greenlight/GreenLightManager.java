package us.jcedeno.game.games.greenlight;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import us.jcedeno.game.games.GameManager;
import us.jcedeno.game.games.greenlight.enums.LightState;
import us.jcedeno.game.games.greenlight.listeners.GreenLightListener;
import us.jcedeno.game.games.greenlight.listeners.PreLightGameListener;
import us.jcedeno.game.games.greenlight.utils.tasks.PlayerArrayQueueShootTask;
import us.jcedeno.game.global.utils.Locations;
import us.jcedeno.game.global.utils.Sounds;
import us.jcedeno.game.players.PlayerManager;

import java.util.ArrayList;

public class GreenLightManager {

    private @Getter final GameManager gManager;
    private @Getter GreenLightGame greenLightGame;

    private @Getter Location firstCubeLocation;
    private @Getter Location secondCubeLocation;
    private @Getter Location cubeCenter;

    private Vector cubeCenter2D;

    private @Getter @Setter LightState lightState;

    private @Getter boolean isRunning;

    private final @Getter PreLightGameListener preGameListener;
    private final @Getter GreenLightListener greenLightListener;

    private @Getter @Setter int greenLowestTimeBound;
    private @Getter @Setter int greenHighestTimeBound;

    private @Getter @Setter int redLowestTimeBound;
    private @Getter @Setter int redHighestTimeBound;

    private @Getter final World world;

    public GreenLightManager(GameManager gManager, World world) {
        this.gManager = gManager;
        this.preGameListener = new PreLightGameListener(this);
        this.greenLightListener = new GreenLightListener(this);
        this.world = world;
        this.greenLightGame = new GreenLightGame(this);
    }

    public GreenLightManager(GameManager gManager, int greenLowestTimeBound, int greenHighestTimeBound, int redLowestTimeBound, int redHighestTimeBound, World world) {
        this.gManager = gManager;
        this.preGameListener = new PreLightGameListener(this);
        this.greenLightListener = new GreenLightListener(this);
        this.greenLowestTimeBound = greenLowestTimeBound;
        this.greenHighestTimeBound = greenHighestTimeBound;
        this.redLowestTimeBound = redLowestTimeBound;
        this.redHighestTimeBound = redHighestTimeBound;
        this.world = world;
        this.greenLightGame = new GreenLightGame(this);
    }

    public void preStart() {
        this.lightState = LightState.PRE_START;

        gManager.getSquidInstance().registerListener(preGameListener);

        //TODO EXTRA FUNCTIONS.
    }

    public void runGame(int seconds) {
        if (this.isRunning) throw new IllegalStateException("The game " + greenLightGame.getClass().getSimpleName() + " is already running.");

        this.isRunning = true;

        //TODO TIMER START;

        greenLightGame.setTaskID(greenLightGame.runTaskTimer(gManager.getSquidInstance(), 0, 20).getTaskId());

        greenLightGame.greenLight(true);

        greenLightGame.setEndTaskID(Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::endGame, seconds * 20L).getTaskId());
    }

    public void endGame() {

        //TODO TIMER END.

        greenLightGame.cancel();
        this.isRunning = false;

        if (this.lightState.equals(LightState.GREEN_LIGHT)) gManager.getSquidInstance().registerListener(greenLightListener);

        greenLightGame.setShootAllTaskID(Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::shootAll, 20 * 10).getTaskId());
    }

    public void stopGame() {
        Bukkit.getScheduler().cancelTask(greenLightGame.getEndTaskID());
        Bukkit.getScheduler().cancelTask(greenLightGame.getShootAllTaskID());

        //TODO TIMER END.

        greenLightGame.cancel();

        gManager.getSquidInstance().unregisterListener(greenLightListener);

        this.greenLightGame = new GreenLightGame(this);
    }

    public void shoot(Player player) {
        if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE) || playerManager().isDead(player)) return;
        Sounds.playSoundDistance(cubeCenter, 150, "sfx.dramatic_gun_shots", 1f, 1f);
        player.setHealth(0);
    }

    public void shootAll() {
        ArrayList<Player> playerList = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (playerManager().isPlayer(p) && !playerManager().isDead(p)
                    && Locations.isInCube(firstCubeLocation.clone().add(21, 0, 0), secondCubeLocation, p.getLocation())) {
                playerList.add(p);
            }
        });
        new PlayerArrayQueueShootTask(this, playerList, 0, 40);
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public boolean inCube(Location location) {
        return Locations.isInCube(firstCubeLocation, secondCubeLocation, location);
    }

    public Vector getCubeCenter2D() {
        return cubeCenter2D.clone();
    }

}
