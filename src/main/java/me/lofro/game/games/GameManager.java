package me.lofro.game.games;

import me.lofro.game.SquidGame;
import me.lofro.game.data.types.GData;
import me.lofro.game.data.utils.JsonConfig;
import me.lofro.game.games.greenlight.commands.GreenLightCMD;
import me.lofro.game.games.greenlight.types.GLightData;
import me.lofro.game.global.interfaces.Restorable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;
import me.lofro.game.games.greenlight.GreenLightManager;
import me.lofro.game.global.utils.extras.BukkitTimer;

/**
 * A class to manage all commands, objects, events, & listeners for each game in
 * the plugin.
 *
 */
public class GameManager extends Restorable<SquidGame> {

    private final @Getter SquidGame squidInstance;

    private final @Getter BukkitTimer bukkitTimer;

    private GData gData;
    private GreenLightManager gLManager;

    public GameManager(final SquidGame squidInstance) {
        this.squidInstance = squidInstance;
        // restore data from dManager json files.
        this.restore(squidInstance.getDManager().gDataConfig());
        // initialize the GreenLightManager.
        this.gLManager = new GreenLightManager(this, Bukkit.getWorlds().get(0));
        // initialize the Timer.
        this.bukkitTimer = BukkitTimer.bTimer(0);

        squidInstance.registerCommands(squidInstance.getCommandManager(),
                new GreenLightCMD(gLManager));
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        // TODO: Fix whatever the fuck this is.
        this.gData = (SquidGame.gson().fromJson(jsonConfig.getJsonObject(), GData.class).gLightData() != null)
                ? SquidGame.gson().fromJson(jsonConfig.getJsonObject(), GData.class)
                : new GData(new GLightData(new Location(Bukkit.getWorlds().get(0), -20, -29, -35),
                        new Location(Bukkit.getWorlds().get(0), -146, 3, 18)));
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(SquidGame.gson().toJsonTree(gData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the gData object.
     */
    public GData gData() {
        return this.gData;
    }

}
