package us.jcedeno.game.global.interfaces;

import us.jcedeno.game.data.utils.JsonConfig;

public abstract class Restorable {

    protected abstract void restore(JsonConfig jsonConfig);
    protected abstract void save(JsonConfig jsonConfig);

}
