package us.jcedeno.game.data;

import us.jcedeno.game.Squid;
import us.jcedeno.game.data.utils.JsonConfig;

/**
 * A class to manage, backup, & restore, the state of the application.
 * 
 * @author jcedeno
 */
public class DataManager {
    private JsonConfig pDataConfig;
    private JsonConfig gDataConfig;

    public DataManager(final Squid instance) throws Exception {
        this.pDataConfig = new JsonConfig("pdata.json", instance.getDataFolder().getAbsolutePath());
        this.gDataConfig = new JsonConfig("gdata.json", instance.getDataFolder().getAbsolutePath());
    }

    public JsonConfig pDataConfig() {
        return pDataConfig;
    }

    public JsonConfig gDataConfig() {
        return gDataConfig;
    }

}
