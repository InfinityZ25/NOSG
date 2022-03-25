package us.jcedeno.game.data;

import us.jcedeno.game.SquidGame;
import us.jcedeno.game.data.commands.DataCMD;
import us.jcedeno.game.data.enums.SquidDataType;
import us.jcedeno.game.data.utils.JsonConfig;
import us.jcedeno.game.global.interfaces.Instantiable;

/**
 * A class to manage, backup, & restore, the state of the application.
 * 
 * @author jcedeno
 */
public class DataManager extends Instantiable<SquidGame> {
    private final JsonConfig pDataConfig;
    private final JsonConfig gDataConfig;

    /**
     * Default constructor for bukkit instantiation.
     * 
     * @param instance The plugin instance.
     * @throws Exception If any of the config files cannot be created at runtime.
     */
    public DataManager(final SquidGame instance) throws Exception {
        super(instance);
        this.pDataConfig = JsonConfig.cfg("pdata.json", instance);
        this.gDataConfig = JsonConfig.cfg("gdata.json", instance);

        ins().registerCommands(ins().getCommandManager(), new DataCMD(this));
    }

    /**
     * A method that saves the current state of the application to json files.
     */
    public void save() {
        ins().getPManager().save(pDataConfig);
        ins().getGManager().save(gDataConfig);
    }

    /**
     * Function to save a specific data config.
     *
     * @param squidDataType plugin data type.
     */
    public void save(SquidDataType squidDataType) {
        switch (squidDataType) {
            case GAME_DATA -> ins().getGManager().save(gDataConfig);
            case PLAYER_DATA -> ins().getPManager().save(pDataConfig);
        }
    }

    /**
     * @return the pDataConfig.
     */
    public JsonConfig pDataConfig() {
        return pDataConfig;
    }

    /**
     * @return the gDataConfig.
     */
    public JsonConfig gDataConfig() {
        return gDataConfig;
    }

}
