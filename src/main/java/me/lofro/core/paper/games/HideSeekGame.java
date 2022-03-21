package me.lofro.core.paper.games;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.Game;
import me.lofro.core.paper.Main;
import org.bukkit.Bukkit;

public class HideSeekGame {

    private final Main instance;
    private final Game game;

    private @Getter boolean isRunning;

    private @Getter int hideLaterTaskID;
    private @Getter int seekLaterTaskID;

    private int hideTime;
    private int seekTime;

    private @Getter @Setter HideGameState hideGameState;

    public HideSeekGame(Main instance, Game game) {
        this.instance = instance;
        this.game = game;
        this.hideGameState = HideGameState.NONE;
    }

    public void runGame(int hideTime, int seekTime) {
        changeMode(HideGameState.HIDE, hideTime);
        this.isRunning = true;

        this.hideTime = hideTime;
        this.seekTime = seekTime;

        this.hideLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, () -> {
            changeMode(HideGameState.SEEK, seekTime);

            this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, this::endGame, seekTime * 20L).getTaskId();
        }, hideTime * 20L).getTaskId();
    }

    public void updateHideTime(int hideTime) {
        this.hideTime = hideTime;

        if (Bukkit.getScheduler().getPendingTasks().stream().anyMatch(t -> t.getTaskId() == seekLaterTaskID)) Bukkit.getScheduler().cancelTask(seekLaterTaskID);
        Bukkit.getScheduler().cancelTask(this.hideLaterTaskID);

        game.getTimer().updateTime(hideTime);

        this.hideLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, () -> {
            changeMode(HideGameState.SEEK, seekTime);

            this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, this::endGame, seekTime * 20L).getTaskId();
        }, hideTime * 20L).getTaskId();
    }

    public void updateSeekTime(int seekTime) {
        this.seekTime = seekTime;

        if (Bukkit.getScheduler().getPendingTasks().stream().noneMatch(t -> t.getTaskId() == hideLaterTaskID)) {
            Bukkit.getScheduler().cancelTask(this.seekLaterTaskID);

            game.getTimer().updateTime(seekTime);

            this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, this::endGame, seekTime * 20L).getTaskId();
        } else {
            Bukkit.getScheduler().cancelTask(this.hideLaterTaskID);

            int currentTime = game.getTimer().getTime((int) game.getGameTime());

            this.hideLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, () -> {
                changeMode(HideGameState.SEEK, seekTime);

                this.seekLaterTaskID = Bukkit.getScheduler().runTaskLater(instance, this::endGame, seekTime * 20L).getTaskId();
            }, currentTime * 20L).getTaskId();
        }
    }

    public void stopGame() {
        hideGameState = HideGameState.NONE;
        game.getTimer().end();

        Bukkit.getScheduler().cancelTask(this.hideLaterTaskID);
        Bukkit.getScheduler().cancelTask(this.seekLaterTaskID);

        this.isRunning = false;
    }

    public void endGame() {
        hideGameState = HideGameState.NONE;
        game.getTimer().end();

        this.isRunning = false;
    }

    public void changeMode(HideGameState hideGameState, int time) {
        game.getTimer().end();
        game.getTimer().start(time);

        this.hideGameState = hideGameState;
    }

    public enum HideGameState {
        HIDE, SEEK, NONE
    }

}
