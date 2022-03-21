package me.lofro.core.paper.games;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import me.lofro.core.paper.listeners.GreenLightListener;
import me.lofro.core.paper.utils.location.Locations;
import me.lofro.core.paper.utils.strings.Strings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

@Data
@EqualsAndHashCode(callSuper = false)
public class GreenLightGame extends BukkitRunnable {

    private Main instance;
    private Game game;

    private @Getter @Setter Location cubeLocation1;
    private @Getter @Setter Location cubeLocation2;
    private @Getter @Setter Location gunLocation;

    private @Getter LightState lightState;

    private boolean isRunning;

    private final GreenLightListener greenLightListener;

    private int timeBetween;

    private final @Getter World world;

    private int seconds;
    private @Getter int taskID;
    private @Getter int runLaterTaskID;

    public GreenLightGame(Main instance, World world, Game game) {
        this.instance = instance;
        this.world = world;
        this.game = game;
        this.greenLightListener = new GreenLightListener(instance, game);
        this.timeBetween = ThreadLocalRandom.current().nextInt(3, 10);
    }

    @Override
    public void run() {
        if (timeBetween < 0) this.timeBetween = ThreadLocalRandom.current().nextInt(3, 10);

        if (timeBetween == 0) {
            greenLight(!lightState.equals(LightState.GREEN_LIGHT));
        }

        if (greenLightListener.getMovedList().size() > 0) {
            new PlayerArrayQueueShootTask(instance, greenLightListener.getMovedList(), false, 10, 20);
            greenLightListener.getMovedList().clear();
        }

        timeBetween--;
    }

    public void runGame(int seconds) {
        if (this.isRunning) return;

        this.seconds = seconds;
        this.isRunning = true;

        game.getTimer().start(seconds);

        greenLight(true);

        this.taskID = this.runTaskTimer(instance, 0, 20).getTaskId();

        this.runLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, () -> {

            game.getTimer().end();

            this.cancel();

            if (this.lightState.equals(LightState.GREEN_LIGHT)) instance.registerListener(greenLightListener);

            Bukkit.getScheduler().runTaskLater(instance, () -> shootAll(true), 20*10);

        }, seconds * 20L).getTaskId();
    }

    public void greenLight(Boolean bool) {
        if (bool) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.showTitle(Title.title(Component.text(Strings.format("&a¡LUZ VERDE!")), Component.text(Strings.format("&aPuedes comenzar a moverte.")),
                        Title.Times.of(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(3))));
                p.playSound(p.getLocation(), "sfx.bell", 1, 1);
            });

            this.lightState = LightState.GREEN_LIGHT;
            instance.unregisterListener(greenLightListener);
        } else {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.showTitle(Title.title(Component.text(Strings.format("&c¡LUZ ROJA!")), Component.text(Strings.format("&cNo muevas ni un pelo.")),
                        Title.Times.of(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(3))));
                p.playSound(p.getLocation(), "sfx.bell", 1, 1);
            });

            Bukkit.getScheduler().runTaskLater(instance, () -> {
                this.lightState = LightState.RED_LIGHT;
                instance.registerListener(greenLightListener);
            },15);
        }
    }

    public void shoot(Player player) {
        if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE)) return;
        game.playSoundDistance(gunLocation,150, "sfx.dramatic_gun_shots", 1f, 1f);
        player.setHealth(0);
    }

    public void shootAll(boolean endGame) {
        ArrayList<Player> playerList = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (game.isPlayer(p) && Locations.isInCube(cubeLocation1.clone().add(21, 0, 0), cubeLocation2, p.getLocation())) {
                playerList.add(p);
            }
        });
        new PlayerArrayQueueShootTask(instance, playerList, endGame, 0, 40);
    }

    public void loadGameCube() {
        setCubeLocation1((cubeLocation1 != null) ? cubeLocation1 : new Location(world, -20, -29, -35));
        setCubeLocation2((cubeLocation2 != null) ? cubeLocation2 : new Location(world,  -146, 3, 18));
        setGunLocation((gunLocation != null) ? gunLocation : new Location(world, (-20 + (-146)) / 2f, (-29 + 3) / 2f, (-35 + 18) / 2f));
    }

    private enum LightState {
        GREEN_LIGHT, RED_LIGHT
    }

    private class PlayerArrayQueueShootTask implements Runnable {

        private final Queue<Player> playerQueue = new ArrayDeque<>();
        private final BukkitTask task;
        private final boolean endGame;

        public PlayerArrayQueueShootTask(JavaPlugin plugin, List<Player> players, boolean endGame, int delay, int period) {
            playerQueue.addAll(players);
            this.task = Bukkit.getScheduler().runTaskTimer(plugin, this, delay, period);
            this.endGame = endGame;
        }

        @Override
        public void run() {
            if (playerQueue.isEmpty()) {
                if (endGame) {
                    instance.unregisterListener(greenLightListener);

                    GreenLightGame newGreenLightGame = new GreenLightGame(instance, world, game);
                    newGreenLightGame.loadGameCube();
                    newGreenLightGame.setRunning(false);

                    game.setGreenLightGame(newGreenLightGame);

                    instance.loadGreenLightData();
                }
                this.task.cancel();
            } else {
                shoot(playerQueue.poll());
            }
        }
    }

}
