package me.dewrs.Managers;

import me.dewrs.Model.Cuboid;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.TreeMap;

public class SafeZoneManager {
    private ProtectionPvP plugin;
    public SafeZoneManager(ProtectionPvP plugin){
        this.plugin = plugin;
    }

    public Location getNearLocation(Player player, ZonePvP zone){
        //Crea una nueva área en la que el jugador puede ser teletransportado
        Location playerLocation = player.getLocation();
        Location newCorner1 = new Location(player.getWorld(), zone.getxMin()-1,
                zone.getyMin()-1,
                zone.getzMin()-1);
        Location newCorner2 = new Location(player.getWorld(), zone.getxMax()+1,
                zone.getyMax()+1,
                zone.getzMax()+1);
        Cuboid kickZone = new Cuboid(newCorner1, newCorner2);
        //Calcula cuál es la ubicación más cercana
        TreeMap<Double, Location> distanceLocations = new TreeMap<>();
        for(Location walls : kickZone.getWalls()){
            double distance = playerLocation.distance(walls);
            if(!distanceLocations.containsKey(distance)){
                distanceLocations.put(playerLocation.distance(walls), walls);
            }
        }
        if(distanceLocations.isEmpty()){
            return null;
        }
        return distanceLocations.firstEntry().getValue();
    }

    public boolean isSafeLocation(Location location){
        if(location.getWorld() == null){
            return false;
        }
        Block blockUp = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
        Block blockFloor = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY()-1, location.getBlockZ());
        return location.getWorld().getBlockAt(location).getType() == Material.AIR &&
                blockUp.getType() == Material.AIR &&
                blockFloor.getType() != Material.AIR &&
                blockFloor.getType().isSolid();
    }

    public ArrayList<Block> getBlocksEmpty(Location location){
        if(location.getWorld() == null){
            return null;
        }
        int i = location.getWorld().getMaxHeight();
        ArrayList<Block> safeBlocks = new ArrayList<>();
        while (i != location.getWorld().getMinHeight()){
            Block actualBlock = location.getWorld().getBlockAt(location.getBlockX(), i, location.getBlockZ());
            if(actualBlock.getType() == Material.AIR) {;
                safeBlocks.add(actualBlock);
            }
            i--;
        }
        return safeBlocks;
    }

    public Location getNearSafeLocation(Player player, Location location){
        ArrayList<Block> safeBlocks = getBlocksEmpty(location);
        Location playerLocation = player.getLocation();
        TreeMap<Double, Location> safeLocations = new TreeMap<>();
        for(Block b : safeBlocks){
            Location auxLocation = new Location(b.getWorld(), b.getX(), b.getY(), b.getZ());
            if(isSafeLocation(auxLocation)){
                safeLocations.put(playerLocation.distance(auxLocation), auxLocation);
            }
        }
        if(safeLocations.isEmpty()){
            return null;
        }
        return safeLocations.firstEntry().getValue();
    }
}
