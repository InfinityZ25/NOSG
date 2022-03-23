package us.jcedeno.game.games.glight.cannons;

import java.util.ArrayList;

import org.bukkit.util.Vector;

import lombok.AllArgsConstructor;
import me.lofro.core.paper.utils.LineVector;

public class Testing {

    /**
     * Given a cube (vector<x, y, z> * 2) with an upper left and lower right corner.
     * 
     * and given a series of cannons (C[]), when a player moves given a redligth,
     * shoot the player from closest cannon.
     * 
     * 
     */
    public static void main(String[] args) {
        var listOfCannons = new ArrayList<LocObject>();
        int n = 32;
        // Generate a bunch of random positions
        for (int i = 0; i < n; i++) {
            listOfCannons.add(LocObject.inBounds(-10, 200, -10, 200));
        }
        // Define a playerLoc
        LocObject playerLoc = LocObject.inBounds(-10, 200, -10, 200);
        // Print out player's location
        System.out.println("PLOC: " + playerLoc.toString());

        cannonball(listOfCannons, playerLoc);

    }

    static void cannonball(ArrayList<LocObject> cannonList, LocObject player) {
        // var cuboid = Cube.of(LocObject.of(0, 0, 0), LocObject.of(120, 100, 120));

        var cannonIter = cannonList.iterator();
        var greatestDistance = Double.MAX_VALUE;
        LocObject closestCannon = null;

        while (cannonIter.hasNext()) {
            var next = cannonIter.next();
            var d = secondDistance(next, player);
            if (d < greatestDistance) {
                // Print out old greatest distance
                System.out.println("GD: " + greatestDistance);
                greatestDistance = d;
                closestCannon = next;
            }
        }

        if (closestCannon != null) {
            System.out.println("closest cannon is " + closestCannon);
            displayParticleTrail(player, closestCannon);

        } else {
            System.out.println("no cannon found");
        }

    }

    static void displayParticleTrail(LocObject player, LocObject cannon) {
        var lineVector = LineVector.of(player.toVector(), cannon.toVector());
        var points = lineVector.getPointsInBetween(0.05);
        for (Vector point : points) {
            System.out.println("point: " + point.toBlockVector().toString());
        }

    }

    double distanceFormula(LocObject a, LocObject b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.z - b.z, 2));
    }

    static double secondDistance(LocObject a, LocObject b) {
        return (Math.abs(a.x - b.x) + Math.abs(a.z - b.z));
    }

    public static @AllArgsConstructor(staticName = "of") class LocObject {
        double x, y, z;

        // Static constructor that generates random locations given some bounds
        static LocObject inBounds(double minX, double maxX, double minZ, double maxZ) {
            return new LocObject(minX + Math.random() * (maxX - minX), minZ + Math.random() * (maxZ - minZ));
        }

        private LocObject(double x, double z) {
            this.x = x;
            this.y = 0;
            this.z = z;
        }

        public Vector toVector() {
            return new Vector(x, y, z);
        }

        @Override
        public String toString() {
            // Return a json formatted location
            return String.format("{\"x\": %f, \"y\": %f, \"z\": %f}", x, y, z);
        }

    }

    // A class that takes 2 LocObjects as the corners of a virtual cuboid
    public static @AllArgsConstructor(staticName = "of") class Cube {
        LocObject upperLeft, lowerRight;
    }

}
