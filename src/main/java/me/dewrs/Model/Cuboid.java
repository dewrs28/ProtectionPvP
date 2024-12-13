package me.dewrs.Model;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class Cuboid {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;
    private final World world;

    public Cuboid(final Location point1, final Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
        this.world = point1.getWorld();
    }

    public boolean isIn(final Location loc) {
        return loc.getWorld() == this.world && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
                .getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }

    public boolean isInWithoutY(final Location loc) {
        return loc.getWorld() == this.world && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax
                && loc.getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }

    public List<Location> getWalls() {
        List<Location> wallLocations = new ArrayList<>();

        int yMinWorld = world.getMinHeight();
        int yMaxWorld = world.getMaxHeight();
        // Añade las paredes laterales (frente y detrás)
        for (int y = yMinWorld; y <= yMaxWorld; y++) {
            for (int z = zMin; z <= zMax; z++) {
                wallLocations.add(new Location(world, xMin, y, z));
                wallLocations.add(new Location(world, xMax, y, z));
            }
        }

        // Añade las paredes laterales (izquierda y derecha)
        for (int y = yMinWorld; y <= yMaxWorld; y++) {
            for (int x = xMin; x <= xMax; x++) {
                wallLocations.add(new Location(world, x, y, zMin));
                wallLocations.add(new Location(world, x, y, zMax));
            }
        }
        return wallLocations;
    }

    public List<Location> getLocationsIn(){
        List<Location> blocks = new ArrayList<>();

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    blocks.add(new Location(world, x, y, z));
                }
            }
        }
        return blocks;
    }

    public int getxMin() {
        return xMin;
    }

    public int getzMin() {
        return zMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getzMax() {
        return zMax;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }

}
