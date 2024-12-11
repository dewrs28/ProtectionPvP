package me.dewrs.Model;

import org.bukkit.Location;

public class ZonePvP extends Cuboid{
    private String name;
    private final String world;
    private final Location corner1;
    private final Location corner2;
    private Location tp;

    public ZonePvP(String name, String world, Location corner1, Location corner2) {
        super(corner1,corner2);
        this.name = name;
        this.world = world;
        this.corner1 = corner1;
        this.corner2 = corner2;
        tp = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public Location getTp() {
        return tp;
    }

    public boolean hasTp(){
        return tp != null;
    }

    public void setTp(Location tp) {
        this.tp = tp;
    }
}
