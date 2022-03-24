package us.jcedeno.game.data;

import us.jcedeno.game.SquidGame;
import us.jcedeno.game.data.commands.DataCMD;
import us.jcedeno.game.data.enums.SquidDataType;
import us.jcedeno.game.data.utils.JsonConfig;

/**
 * A class to manage, backup, & restore, the state of the application.
 * 
 * @author jcedeno
 */
public class DataManager {
    private final JsonConfig pDataConfig;
    private final JsonConfig gDataConfig;

    public DataManager(final SquidGame instance) throws Exception {
        this.pDataConfig = new JsonConfig("pdata.json", instance.getDataFolder().getAbsolutePath());
        this.gDataConfig = new JsonConfig("gdata.json", instance.getDataFolder().getAbsolutePath());

        SquidGame.getInstance().registerCommands(SquidGame.getInstance().getCommandManager(), new DataCMD(this));
    }

    public JsonConfig pDataConfig() {
        return pDataConfig;
    }

    public JsonConfig gDataConfig() {
        return gDataConfig;
    }

    /**
     * A method that saves the current state of the application to json files.
     */
    public void save() {
        SquidGame.getInstance().getPManager().save(pDataConfig);
        SquidGame.getInstance().getGManager().save(gDataConfig);
    }

    /**
     * Function to save a specific data config.
     *
     * @param squidDataType plugin data type.
     */
    public void save(SquidDataType squidDataType) {
        switch (squidDataType) {
            case GAME_DATA -> SquidGame.getInstance().getGManager().save(gDataConfig);
            case PLAYER_DATA -> SquidGame.getInstance().getPManager().save(pDataConfig);
        }
    }

}
