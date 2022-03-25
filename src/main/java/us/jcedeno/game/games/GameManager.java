package us.jcedeno.game.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;
import us.jcedeno.game.SquidGame;
import us.jcedeno.game.data.types.GData;
import us.jcedeno.game.data.utils.JsonConfig;
import us.jcedeno.game.games.greenlight.GreenLightManager;
import us.jcedeno.game.games.greenlight.commands.GreenLightCMD;
import us.jcedeno.game.games.greenlight.types.GLightData;
import us.jcedeno.game.global.interfaces.Restorable;
import us.jcedeno.game.global.utils.extras.BukkitTimer;

/**
 * A class to manage all commands, objects, events, & listeners for each game in
 * the plugin.
 * 
 * @author jcedeno
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
