package us.jcedeno.game.games.greenlight.types;

import java.util.ArrayList;
import java.util.List;

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
    @Getter
    List<Location> cannonLocations;

    public GLightData(Location cuberUpper, Location cubeLower, Location... cannonLocations) {
        this.cuberUpper = cuberUpper;
        this.cubeLower = cubeLower;
        this.cannonLocations = new ArrayList<>();
        for (Location loc : cannonLocations) {
            this.cannonLocations.add(loc);
        }
    }

    public GLightData(Location cubeUpper, Location cubeLower, List<Location> cannonLocations) {
        this.cuberUpper = cubeUpper;
        this.cubeLower = cubeLower;
        this.cannonLocations = cannonLocations;
    }
}
