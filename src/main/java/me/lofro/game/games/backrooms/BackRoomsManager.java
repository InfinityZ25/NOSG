package me.lofro.game.games.backrooms;

import lombok.Getter;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.backrooms.enums.BackRoomsState;
import me.lofro.game.games.backrooms.events.BackRoomsChangeStateEvent;
import me.lofro.game.games.backrooms.listeners.BackRoomsListener;
import me.lofro.game.games.backrooms.listeners.PreBackRoomsListener;
import me.lofro.game.games.backrooms.types.BackRoomsData;
import me.lofro.game.global.utils.Locations;
import me.lofro.game.global.utils.Strings;
import me.lofro.game.players.PlayerManager;
import me.lofro.game.players.objects.SquidPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class BackRoomsManager {

    private final GameManager gManager;

    private final @Getter BackRoomsListener backRoomsListener;
    private final @Getter PreBackRoomsListener preBackRoomsListener;

    private final @Getter HashMap<String, SquidPlayer> winners = new HashMap<>();

    private @Getter BackRoomsState backRoomsState;

    private @Getter boolean isRunning = false;

    private @Getter int winnerLimit;

    private int safeSeconds;

    private int changeStateLaterTaskID;

    private final @Getter World world;

    private Vector middleCubeCenter2D;
    private @Getter Location middleCubeCenter;

    private Vector centerGoal2D;
    private @Getter Location centerGoal;

    private final @Getter String gamePrefix = Strings.format("&6&lBack&e&lRooms &7>> &r");

    public BackRoomsManager(GameManager gameManager, World world) {
        this.gManager = gameManager;
        this.world = world;
        this.middleCubeCenter = Locations.getCubeCenter(world, middleCubeLower(), middleCubeUpper());
        this.centerGoal = Locations.getCubeCenter(world, backRoomsData().getGoalLower(), backRoomsData().getGoalUpper());
        setMiddleCubeCenter(middleCubeCenter);
        setCenterGoal2D(centerGoal);
        this.preBackRoomsListener = new PreBackRoomsListener(this);
        this.backRoomsListener = new BackRoomsListener(this);
    }

    public void preGame() {
        changeState(BackRoomsState.PRE_START);

        gManager.getSquidInstance().registerListener(preBackRoomsListener);
    }

    public void runGame(int safeSeconds, int playerLimit) {
        this.safeSeconds = safeSeconds;
        this.winnerLimit = playerLimit;
        this.isRunning = true;

        this.winners.clear();

        gManager.getSquidInstance().unregisterListener(preBackRoomsListener);
        gManager.getSquidInstance().registerListener(backRoomsListener);

        changeState(BackRoomsState.SAFE);

        gManager.getTimer().start(safeSeconds);

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.playSound(p.getLocation(), "sfk.backrooms_intro", 1f, 1f);

            if (!playerManager().isPlayer(p)) return;

            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
        });

        this.changeStateLaterTaskID = Bukkit.getScheduler().runTaskLater
                (gManager.getSquidInstance(), () -> {
                    changeState(BackRoomsState.HUNTING);
                    Bukkit.broadcast(Component.text(Strings.format(gamePrefix + "&cLas bestias han sido liberadas.")));
                }, (safeSeconds + 2) * 20L).getTaskId();
    }

    public void stopGame() {
        Bukkit.getScheduler().cancelTask(changeStateLaterTaskID);

        this.isRunning = false;

        changeState(BackRoomsState.NONE);

        gManager.getTimer().end();

        gManager.getSquidInstance().unregisterListener(backRoomsListener);
    }

    public void endGame() {
        var losers = Bukkit.getOnlinePlayers().stream().filter(p -> !winners.containsKey(p.getName()));

        losers.forEach(l -> {
            if (!playerManager().isPlayer(l)) return;
            l.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1, false, false));
        });

        Bukkit.getScheduler().runTaskLater(gameManager().getSquidInstance(), () -> {
            changeState(BackRoomsState.NONE);
            gameManager().getSquidInstance().unregisterListener(backRoomsListener);
            this.isRunning = false;
        }, 10 * 20L);
    }

    public void changeState(BackRoomsState backRoomsState) {
        this.backRoomsState = backRoomsState;
        Bukkit.getPluginManager().callEvent(new BackRoomsChangeStateEvent(backRoomsState));
    }

    public GameManager gameManager() {
        return gManager;
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public BackRoomsData backRoomsData() {
        return gManager.gData().backRoomsData();
    }

    public Location middleCubeLower() {
        return backRoomsData().getMiddleCubeLower();
    }

    public Location middleCubeUpper() {
        return backRoomsData().getMiddleCubeUpper();
    }

    public boolean inGoal(Location location) {
        return Locations.isInCube(backRoomsData().getGoalLower(),backRoomsData().getGoalUpper(), location);
    }

    public boolean isMiddleCube(Location location) {
        return Locations.isInCube(middleCubeLower(), middleCubeUpper(), location);
    }

    public void setMiddleCubeCenter(Location location) {
        this.middleCubeCenter = location;
        this.middleCubeCenter2D = new Vector(location.getX(), 0, location.getZ());
    }

    public void setCenterGoal2D(Location location) {
        this.centerGoal = location;
        this.centerGoal2D = new Vector(location.getX(), 0, location.getZ());
    }

    public Vector middleCubeCenter2D() {
        return this.middleCubeCenter2D.clone();
    }

    public Vector goalCenter2D() {
        return this.centerGoal2D.clone();
    }

}
