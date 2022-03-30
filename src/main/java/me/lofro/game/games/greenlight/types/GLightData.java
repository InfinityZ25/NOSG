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

    @Getter @Setter Location cubeUpper, cubeLower, standLocation;
    @Getter List<Location> cannonLocations;

    public GLightData() {
        final var baseWorld = Bukkit.getWorlds().get(0);
        this.cubeLower = new Location(baseWorld, -20, -29, -35);
        this.cubeUpper = new Location(baseWorld, -146, 15, 18);
        this.standLocation = new Location(baseWorld, -152,-28,-9);
        this.cannonLocations = new ArrayList<>();
    }

    public GLightData(Location cuberUpper, Location cubeLower, Location standLocation, Location... cannonLocations) {
        this.cubeUpper = cuberUpper;
        this.cubeLower = cubeLower;
        this.standLocation = standLocation;
        this.cannonLocations = new ArrayList<>();
        this.cannonLocations.addAll(Arrays.asList(cannonLocations));
    }

    public GLightData(Location cubeUpper, Location cubeLower, Location standLocation, List<Location> cannonLocations) {
        this.cubeUpper = cubeUpper;
        this.cubeLower = cubeLower;
        this.standLocation = standLocation;
        this.cannonLocations = cannonLocations;
    }
}
