package me.lofro.game.games.backrooms.types;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BackRoomsData {

    private @Getter @Setter Location middleCubeLower, middleCubeUpper, cubeLower, cubeUpper;

    public BackRoomsData() {
        final var baseWorld = Bukkit.getWorlds().get(0);

        this.cubeLower = new Location(baseWorld, 287,-44,-153);
        this.cubeUpper = new Location(baseWorld, 89,-40,12);
        this.middleCubeLower = new Location(baseWorld, 90,-43,-90);
        this.middleCubeUpper = new Location(baseWorld, 132,-39,-54);
    }

    public BackRoomsData( Location cubeLower, Location cubeUpper, Location middleCubeLower, Location middleCubeUpper) {
        this.cubeLower = cubeLower;
        this.cubeUpper = cubeUpper;
        this.middleCubeLower = middleCubeLower;
        this.middleCubeUpper = middleCubeUpper;
    }

}
