package me.lofro.game.games.purge.types;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PurgeData {

    private @Getter @Setter Location areaLower, areaUpper, foodLocation;

    public PurgeData() {
        var baseWorld = Bukkit.getWorlds().get(0);

        this.foodLocation = new Location(baseWorld, -23,-50,-25);
        this.areaLower = new Location(baseWorld, -66,-51,22);
        this.areaUpper = new Location(baseWorld, 20,-34,-44);
    }

    public PurgeData(Location areaLower, Location areaUpper, Location foodLocation) {
        this.areaLower = areaLower;
        this.areaUpper = areaUpper;
        this.foodLocation = foodLocation;
    }

}
