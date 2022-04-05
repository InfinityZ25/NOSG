package me.lofro.game.games.deathnote;

import lombok.Getter;
import lombok.Setter;
import me.lofro.game.SquidGame;
import me.lofro.game.games.GameManager;
import me.lofro.game.games.deathnote.listeners.DeathNoteListener;
import me.lofro.game.games.deathnote.types.DeathNoteData;
import me.lofro.game.global.enums.PvPState;
import me.lofro.game.global.utils.Locations;
import me.lofro.game.players.PlayerManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeathNoteManager {

    private final @Getter GameManager gManager;

    private final @Getter DeathNoteListener deathNoteListener;

    private @Getter boolean isRunning = false;

    private @Getter @Setter int winnerLimit;

    private final @Getter List<Player> winners = new ArrayList<>();

    private final @Getter World world;

    public DeathNoteManager(GameManager gManager, World world) {
        this.gManager = gManager;
        this.world = world;

        this.deathNoteListener = new DeathNoteListener(this);
    }

    public void runGame(int seconds, int winnerLimit) {
        if (this.isRunning)
            throw new IllegalStateException(
                    "The game " + this.getClass().getSimpleName() + " is already running.");

        this.isRunning = true;
        this.winnerLimit = winnerLimit;
        this.winners.clear();

        gManager.getTimer().start(seconds);

        gManager.gameData().setPvPState(PvPState.ONLY_GUARDS);

        squidGame().registerListener(deathNoteListener);
    }

    public void endGame() {
        this.isRunning = false;
        this.winners.clear();

        gManager.getTimer().end();

        squidGame().unregisterListener(deathNoteListener);
    }

    public SquidGame squidGame() {
        return gManager.getSquidInstance();
    }

    public PlayerManager playerManager() {
        return gManager.getSquidInstance().getPManager();
    }

    public DeathNoteData deathNoteData() {
        return gManager.gameData().deathNoteData();
    }

    public boolean inGoal(Location location) {
        return Locations.isInCube(deathNoteData().getGoalLower(), deathNoteData().getGoalUpper(), location);
    }

}
