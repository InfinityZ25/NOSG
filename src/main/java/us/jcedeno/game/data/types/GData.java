package us.jcedeno.game.data.types;

import lombok.Getter;
import us.jcedeno.game.games.greenlight.types.GLightData;

/**
 * A class designed to hold all the stateful data for all minigames.
 * 
 * @author jcedeno
 */
public class GData {

    private @Getter final GLightData greenLightData;

    public GData(GLightData greenLightData) {
        this.greenLightData = greenLightData;
    }
}
