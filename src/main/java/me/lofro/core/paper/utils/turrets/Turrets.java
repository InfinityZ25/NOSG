package me.lofro.core.paper.utils.turrets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;
import me.lofro.core.paper.utils.LineVector;
import me.lofro.core.paper.utils.turrets.exceptions.TurretListEmptyException;

/**
 * A class that holds the logic for GreenLight/RedLight turrets.
 */
public class Turrets {

    private @Getter List<Location> turretLocations;
    private @Getter @Setter Particle particle = Particle.REDSTONE;

    public Turrets() {
        this.turretLocations = new ArrayList<>();
    }

    /**
     * Function to shoot a player from the closest turret.
     * 
     * @param playerLocation The location of the player.
     * @throws TurretListEmptyException If the turret list is empty.
     */
    public void shootPlayerFromTurrent(Location playerLocation) throws TurretListEmptyException {
        if (turretLocations.isEmpty()) {
            throw new TurretListEmptyException();
        }
        final var closestTurret = getClosestTurret(playerLocation, turretLocations.iterator());
        // Distance to be left betwixt each point of the line vector.
        final double t = 0.5;

        if (closestTurret != null) {
            // Display a particle trail from the player to the closest turret.
            displayParticleTrail(playerLocation, closestTurret, t, particle);
        } else {
            // Throw exception
            throw new TurretListEmptyException();
        }
    }

    /**
     * Utility function to compute the closest turret to a given loc.
     * 
     * @param playerLocation the location of the player.
     * @param iter           the iterator to iterate over the turrets.
     * @return the closest turret to the player.
     */
    private Location getClosestTurret(Location playerLocation, final Iterator<Location> iter) {
        // Use max value so that first iteration doesn't fail.
        var greatestDistance = Double.MAX_VALUE;
        // Use null to indicate no closest turret currently found.
        Location closestTurret = null;
        /*
         * Iterate through the list of turrets and find the one that's closests to the
         * player.
         */
        while (iter.hasNext()) {
            var nextTurret = iter.next();
            final var d = taxiDistanceFormula(nextTurret, playerLocation);
            // If new distance less than previous, update.
            if (d < greatestDistance) {
                greatestDistance = d;
                closestTurret = nextTurret;
            }
        }
        return closestTurret;
    }

    /**
     * Function that spawns particles across a line vector to simulate a smoke
     * trail.
     * 
     * @param player       The player's location.
     * @param cannon       The cannon's location.
     * @param t            The <i,j,k> vector unit to move across.
     * @param particleType The particle type to spawn.
     */
    void displayParticleTrail(Location player, Location cannon, double t, Particle particleType) {
        var lineVector = LineVector.of(player.toVector(), cannon.toVector());
        final var world = player.getWorld();

        var points = lineVector.getPointsInBetween(t);
        for (Vector point : points) {
            world.spawnParticle(particleType, point.toLocation(world), 1);
        }

    }

    /**
     * Calculates distance from two points using traditional (computationally
     * intense) ecluidian distance formula.
     * 
     * @param a first point.
     * @param b second point.
     * @return distance between two points.
     */
    double distanceFormula(Location a, Location b) {
        if (a.getWorld() != b.getWorld()) {
            throw new IllegalArgumentException("Points must be in the same world.");
        }
        var x = a.getX() - b.getX();
        var z = a.getZ() - b.getZ();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
    }

    /**
     * Calculates distance from two points using a more efficient distance formula
     * (taxi-cab distance).
     * 
     * @param a first point.
     * @param b second point.
     * @return distance between two points.
     */
    double taxiDistanceFormula(Location a, Location b) {
        if (a.getWorld() != b.getWorld()) {
            throw new IllegalArgumentException("Points must be in the same world.");
        }
        var x = Math.abs(a.getX() - b.getX());
        var z = Math.abs(a.getZ() - b.getZ());
        return x + z;
    }

}
