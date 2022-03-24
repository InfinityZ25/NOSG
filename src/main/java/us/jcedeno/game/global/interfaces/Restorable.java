package us.jcedeno.game.global.interfaces;

import us.jcedeno.game.data.utils.JsonConfig;

public abstract class Restorable {

    protected abstract void restore(JsonConfig jsonConfig);
    public abstract void save(JsonConfig jsonConfig);

}
