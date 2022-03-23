package us.jcedeno.game.games.greenlight.utils.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import us.jcedeno.game.games.greenlight.GreenLightManager;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class PlayerArrayQueueShootTask implements Runnable {

    private final Queue<Player> playerQueue = new ArrayDeque<>();
    private final BukkitTask task;
    private final GreenLightManager gLightManager;

    public PlayerArrayQueueShootTask(GreenLightManager gLightManager, List<Player> players, int delay, int period) {
        playerQueue.addAll(players);
        this.gLightManager = gLightManager;
        this.task = Bukkit.getScheduler().runTaskTimer(gLightManager.getGManager().getSquidInstance(), this, delay, period);
    }

    @Override
    public void run() {
        if (playerQueue.isEmpty()) {
            this.task.cancel();
        } else {
            gLightManager.shoot(playerQueue.poll());
        }
    }
}
