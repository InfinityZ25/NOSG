package us.jcedeno.game.data.types;

import us.jcedeno.game.games.greenlight.types.GLightData;

/**
 * A class designed to hold all the stateful data for all mini-games.
 * 
 * @author jcedeno
 */
public class GData {

    private final GLightData greenLightData;

    public GData(GLightData greenLightData) {
        this.greenLightData = greenLightData;
    }

    /**
     * @return the greenLightData
     */
    public GLightData gLightData() {
        return greenLightData;
    }

}
