package me.dewrs.Managers;

import me.dewrs.Model.Cuboid;
import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class ZonesManager {
    private ProtectionPvP plugin;
    private ArrayList<ZonePvP> zones;

    public ZonesManager(ProtectionPvP plugin){
        this.plugin = plugin;
    }

    public ArrayList<ZonePvP> getZones() {
        return zones;
    }

    public void setZones(ArrayList<ZonePvP> zones) {
        this.zones = zones;
    }

    public ZonePvP getZoneByName(String name){
        for(ZonePvP zone : zones){
            if(zone.getName().equalsIgnoreCase(name)){
                return zone;
            }
        }
        return null;
    }

    public void setName(ZonePvP zone, String name){
        plugin.getZoneDataManager().renameZoneInConfig(zone, name);
        zone.setName(name);
    }

    public void createNewZone(ZonePvP zone){
        zones.add(zone);
        plugin.getZoneDataManager().writeNewZoneInConfig(zone);
        kickAllPlayersFromZone(zone);
    }

    public void removeZone(ZonePvP zone){
        zones.remove(zone);
        plugin.getZoneDataManager().deleteZoneInConfig(zone);
        plugin.getZoneViewerManager().removeZoneWallsToAllPlayers(zone);
    }

    public void setTpZone(ZonePvP zone, Location location){
        zone.setTp(location);
        plugin.getZoneDataManager().writeTpZoneInConfig(zone, location);
    }

    @SuppressWarnings("ALL")
    public boolean twoCornersSameWorld(Player player) {
        if (!twoCornersSelected(player)) {
            return true;
        }
        return Objects.equals(
                ProtectionPvP.cacheLocCorner1.get(player).getWorld().getName(),
                ProtectionPvP.cacheLocCorner2.get(player).getWorld().getName()
        );
    }

    @SuppressWarnings("ALL")
    public boolean twoCornersSelected(Player player) {
        return ProtectionPvP.cacheLocCorner1.containsKey(player)
                && ProtectionPvP.cacheLocCorner2.containsKey(player);
    }

    public boolean zoneAlreadyExist(String name){
        for(ZonePvP zone : zones){
            if(zone.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public void restorePlayerCornerCache(Player player){
        ProtectionPvP.cacheLocCorner1.remove(player);
        ProtectionPvP.cacheLocCorner2.remove(player);
    }

    public Cuboid getCuboIdDistance(ZonePvP zone){
        int distance = plugin.getConfigManager().getDistanceZonePvP();
        Location corner1 = new Location(Bukkit.getWorld(zone.getWorld()),
                zone.getxMin()-distance,
                zone.getyMin()-distance,
                zone.getzMin()-distance);
        Location corner2 = new Location(Bukkit.getWorld(zone.getWorld()),
                zone.getxMax()+distance,
                zone.getyMax()+distance,
                zone.getzMax()+distance);
        return new Cuboid(corner1, corner2);
    }

    public ArrayList<ZonePvP> getNearbyZones(Location location){
        ArrayList<ZonePvP> nearbyZones = new ArrayList<>();
        for(ZonePvP zone : zones){
            if(this.getCuboIdDistance(zone).isInWithoutY(location)){
                nearbyZones.add(zone);
            }
        }
        return nearbyZones;
    }

    public ArrayList<Player> getPlayersInZone(ZonePvP zone){
        ArrayList<Player> playersInZone = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            if(zone.isInWithoutY(p.getLocation())){
                playersInZone.add(p);
            }
        }
        return playersInZone;
    }

    public void kickAllPlayersFromZone(ZonePvP zone){
        for(Player p : getPlayersInZone(zone)){
            PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(p);
            if(playerData.isProtected()) {
                kickPlayerOfActualZone(p);
            }
        }
    }

    public ZonePvP getActualPlayerZone(Player player){
        for(ZonePvP zone : zones){
            if(zone.isInWithoutY(player.getLocation())){
                return zone;
            }
        }
        return null;
    }

    public void kickPlayerOfActualZone(Player player) {
        Location tpLocation;
        ZonePvP actualZone = plugin.getZonesManager().getActualPlayerZone(player);
        if (actualZone == null) {
            return;
        }
        if(actualZone.hasTp()){
            tpLocation = actualZone.getTp();
            player.teleport(tpLocation);
            return;
        }
        tpLocation = plugin.getSafeZoneManager().getNearLocation(player, actualZone);
        if (tpLocation == null) {
            tpLocation = player.getWorld().getSpawnLocation();
            player.teleport(tpLocation);
            return;
        }
        if (!plugin.getSafeZoneManager().isSafeLocation(tpLocation)) {
            tpLocation = plugin.getSafeZoneManager().getNearSafeLocation(player, tpLocation);
        }
        if (tpLocation == null) {
            tpLocation = player.getWorld().getSpawnLocation();
        }
        player.teleport(tpLocation);
    }

    public boolean isZoneOverlap(Location corner1, Location corner2){
        Cuboid preZone = new Cuboid(corner1, corner2);
        for(ZonePvP z : plugin.getZonesManager().getZones()){
            if (preZone.getxMin() <= z.getxMax() && preZone.getxMax() >= z.getxMin() &&
                    preZone.getzMin() <= z.getzMax() && preZone.getzMax() >= z.getzMin()) {
                return true;
            }
        }
        return false;
    }
}
