package me.lofro.core.paper.utils.location;

import org.bukkit.Location;

public class Locations {

    public static boolean isInCube(Location pos1, Location pos2, Location point) {

        var cX = pos1.getX() < pos2.getX();
        var cY = pos1.getY() < pos2.getY();
        var cZ = pos1.getZ() < pos2.getZ();

        var minX = cX ? pos1.getX() : pos2.getX();
        var maxX = cX ? pos2.getX() : pos1.getX();

        var minY = cY ? pos1.getY() : pos2.getY();
        var maxY = cY ? pos2.getY() : pos1.getY();

        var minZ = cZ ? pos1.getZ() : pos2.getZ();
        var maxZ = cZ ? pos2.getZ() : pos1.getZ();

        if (point.getX() < minX || point.getY() < minY || point.getZ() < minZ)
            return false;
        return !(point.getX() > maxX) && !(point.getY() > maxY) && !(point.getZ() > maxZ);
    }

}
