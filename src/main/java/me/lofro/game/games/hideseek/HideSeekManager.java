package me.lofro.game.games.hideseek;

import lombok.Getter;
import lombok.Setter;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.hideseek.enums.HideGameState;
import me.lofro.game.games.hideseek.events.ChangeHideStateEvent;
import me.lofro.game.games.hideseek.listeners.HideSeekListener;
import org.bukkit.Bukkit;

public class HideSeekManager {

    private final @Getter GameManager gManager;

    private @Getter boolean isRunning = false;

    private @Getter @Setter int hideTime;
    private @Getter @Setter int seekTime;

    private @Getter int hideLaterTaskID;
    private @Getter int seekLaterTaskID;

    private @Getter HideGameState hideGameState;

    public HideSeekManager(GameManager gManager) {
        this.gManager = gManager;
        this.hideGameState = HideGameState.NONE;
        gManager.getSquidInstance().registerListeners(new HideSeekListener(this));
    }

    public void runGame(int hideTime, int seekTime) {
        if (this.isRunning)
            throw new IllegalStateException(
                    "The game " + this.getClass().getSimpleName() + " is already running.");

        changeMode(HideGameState.HIDE, hideTime);

        this.isRunning = true;

        this.hideTime = hideTime;
        this.seekTime = seekTime;

        this.hideLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), () -> {
            changeMode(HideGameState.SEEK, seekTime);

            this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::endGame, seekTime * 20L).getTaskId();
        }, (hideTime + 2) * 20L).getTaskId();
    }

    public void changeMode(HideGameState hideGameState, int time) {
        gManager.getTimer().end();
        gManager.getTimer().start(time);

        this.hideGameState = hideGameState;

        Bukkit.getPluginManager().callEvent(new ChangeHideStateEvent(hideGameState));
    }

    public void updateHideTime(int hideTime) {
        this.hideTime = hideTime;

        Bukkit.getScheduler().cancelTask(this.hideLaterTaskID);
        if (Bukkit.getScheduler().getPendingTasks().stream().anyMatch(t -> t.getTaskId() == seekLaterTaskID)) Bukkit.getScheduler().cancelTask(seekLaterTaskID);

        gManager.getTimer().update(hideTime);

        this.hideLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), () -> {
            changeMode(HideGameState.SEEK, seekTime);

            this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::endGame, seekTime * 20L).getTaskId();
        }, (hideTime + 2) * 20L).getTaskId();
    }

    public void updateSeekTime(int seekTime) {
        this.seekTime = seekTime;

        if (Bukkit.getScheduler().getPendingTasks().stream().noneMatch(t -> t.getTaskId() == hideLaterTaskID)) {
            Bukkit.getScheduler().cancelTask(this.seekLaterTaskID);

            gManager.getTimer().update(seekTime);

            this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::endGame, seekTime * 20L).getTaskId();
        } else {
            Bukkit.getScheduler().cancelTask(this.hideLaterTaskID);

            int currentTime = gManager.getTimer().getTime();

            this.hideLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), () -> {
                changeMode(HideGameState.SEEK, seekTime);

                this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(gManager.getSquidInstance(), this::endGame, seekTime * 20L).getTaskId();
            }, (currentTime + 2) * 20L).getTaskId();
        }
    }

    public void endGame() {
        this.hideGameState = HideGameState.NONE;
        this.isRunning = false;
    }

    public void stopGame() {
        this.hideGameState = HideGameState.NONE;
        gManager.getTimer().end();

        Bukkit.getScheduler().cancelTask(this.hideLaterTaskID);
        Bukkit.getScheduler().cancelTask(this.seekLaterTaskID);

        this.isRunning = false;
    }

}
