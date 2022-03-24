package us.jcedeno.game.games.greenlight.types;

import org.bukkit.Location;

import lombok.Getter;

/**
 * A class designed to hold all the stateful data for GreenLight RedLight
 * minigame.
 * 
 * @author jcedeno
 */
public class GLightData {

    @Getter
    Location cuberUpper, cubeLower;

    public GLightData(Location cuberUpper, Location cubeLower) {
        this.cuberUpper = cuberUpper;
        this.cubeLower = cubeLower;
    }
}
