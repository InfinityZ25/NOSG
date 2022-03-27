package me.lofro.game.games.greenlight.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;

/**
 * A class designed to hold all the stateful data for GreenLight RedLight
 * mini-game.
 *
 */
public class GLightData {

    @Getter @Setter Location cubeUpper, cubeLower;
    @Getter List<Location> cannonLocations;

    public GLightData() {
        final var baseWorld = Bukkit.getWorlds().get(0);
        this.cubeLower = new Location(baseWorld, -20, 0, -35);
        this.cubeUpper = new Location(baseWorld, -146, 15, 18);
        this.cannonLocations = new ArrayList<>();
    }

    public GLightData(Location cuberUpper, Location cubeLower, Location... cannonLocations) {
        this.cubeUpper = cuberUpper;
        this.cubeLower = cubeLower;
        this.cannonLocations = new ArrayList<>();
        this.cannonLocations.addAll(Arrays.asList(cannonLocations));
    }

    public GLightData(Location cubeUpper, Location cubeLower, List<Location> cannonLocations) {
        this.cubeUpper = cubeUpper;
        this.cubeLower = cubeLower;
        this.cannonLocations = cannonLocations;
    }
}
