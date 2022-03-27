package me.lofro.game.games.backrooms.types;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BackRoomsData {

    private @Getter @Setter Location goalLower, goalUpper, middleCubeLower, middleCubeUpper;

    public BackRoomsData() {
        final var baseWorld = Bukkit.getWorlds().get(0);

        this.goalLower = new Location(baseWorld, 99,-44,4);
        this.goalUpper = new Location(baseWorld, 87,-40,7);
        this.middleCubeLower = new Location(baseWorld, 90,-43,-90);
        this.middleCubeUpper = new Location(baseWorld, 132,-39,-54);
    }

    public BackRoomsData(Location goalLower, Location goalUpper, Location middleCubeLower, Location middleCubeUpper) {
        this.goalLower = goalLower;
        this.goalUpper = goalUpper;
        this.middleCubeLower = middleCubeLower;
        this.middleCubeUpper = middleCubeUpper;
    }

}
