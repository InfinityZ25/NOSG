package me.lofro.game.games.finalfight;

import lombok.Getter;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.finalfight.listeners.FinalFightListener;
import me.lofro.game.global.enums.PvPState;
import me.lofro.game.players.PlayerManager;
import org.bukkit.Bukkit;

public class FinalFightManager {

    private final @Getter GameManager gameManager;
    private final @Getter FinalFightListener finalFightListener;

    private @Getter boolean isRunning = false;

    public FinalFightManager(GameManager gameManager) {
        this.gameManager = gameManager;

        this.finalFightListener = new FinalFightListener(this);
    }

    public void runGame() {
        this.isRunning = true;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather thunder");

        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "sfx.final_fight", 1f, 1f));

        gameManager.gameData().setPvPState(PvPState.ALL);

        gameManager.getSquidInstance().registerListener(finalFightListener);
    }

    public void endGame() {
        this.isRunning = false;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");

        gameManager.gameData().setPvPState(PvPState.ONLY_GUARDS);

        gameManager.getSquidInstance().unregisterListener(finalFightListener);
    }

    public PlayerManager playerManager() {
        return gameManager.getSquidInstance().getPManager();
    }

}
