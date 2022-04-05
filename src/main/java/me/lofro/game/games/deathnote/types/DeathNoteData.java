package me.lofro.game.games.deathnote.types;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * A class designed to hold all the stateful data for Death note mini-game.
 */
public class DeathNoteData {

    private @Getter @Setter Location goalLower, goalUpper;

    public DeathNoteData() {
        var baseWorld = Bukkit.getWorlds().get(0);

        this.goalLower = new Location(baseWorld, 43,-35,7);
        this.goalUpper = new Location(baseWorld, 38,-31,11);
    }

    public DeathNoteData(Location goalLower, Location goalUpper) {
        this.goalLower = goalLower;
        this.goalUpper = goalUpper;
    }

}
