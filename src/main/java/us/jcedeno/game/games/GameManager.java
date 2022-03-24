package us.jcedeno.game.games;

import org.bukkit.Bukkit;

import lombok.Getter;
import us.jcedeno.game.SquidGame;
import us.jcedeno.game.data.types.GData;
import us.jcedeno.game.data.utils.JsonConfig;
import us.jcedeno.game.games.greenlight.GreenLightManager;
import us.jcedeno.game.global.interfaces.Restorable;

/**
 * A class to manage all commands, objects, events, & listeners for each game in
 * the plugin.
 * 
 * @author jcedeno
 */
public class GameManager extends Restorable {

    private final @Getter SquidGame squidInstance;

    private GData gData;
    private GreenLightManager gLManager;

    public GameManager(SquidGame squidInstance) {
        this.squidInstance = squidInstance;
        // restore data from dManager json files.
        this.restore(squidInstance.getDManager().gDataConfig());
        // initialize the GreenLightManager.
        this.gLManager = new GreenLightManager(this, Bukkit.getWorlds().get(0));
    }

    @Override
    protected void restore(JsonConfig jsonConfig) {
        this.gData = SquidGame.gson().fromJson(jsonConfig.getJsonObject(), GData.class);
        // TODO Make this work with GreenLightManager.
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
