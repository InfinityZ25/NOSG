package me.lofro.game.games.parkour.types;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * A class designed to hold all the stateful data for Parkour mini-game.
 */
public class ParkourData {

    private @Getter @Setter Location goalLower, goalUpper, areaLower, areaUpper;

    public ParkourData() {
        var baseWorld = Bukkit.getWorlds().get(0);

        this.goalLower = new Location(baseWorld, -87,-28,-111);
        this.goalUpper = new Location(baseWorld, -83,-24,-105);
        this.areaLower = new Location(baseWorld, -16,-32,-93);
        this.areaUpper = new Location(baseWorld, -93,-15,-151);
    }

    public ParkourData(Location goalLower, Location goalUpper, Location areaLower, Location areaUpper) {
        this.goalLower = goalLower;
        this.goalUpper = goalUpper;
        this.areaLower = areaLower;
        this.areaUpper = areaUpper;
    }

}
