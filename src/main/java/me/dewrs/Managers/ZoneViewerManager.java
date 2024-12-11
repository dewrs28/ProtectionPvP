package me.dewrs.Managers;

import me.dewrs.Model.Cuboid;
import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class ZoneViewerManager {
    private final ProtectionPvP plugin;
    private HashMap<Player, ArrayList<ZonePvP>> playersViewingZones;
    private HashMap<Player, ArrayList<Location>> playersBlocksViewing;

    public ZoneViewerManager(ProtectionPvP plugin) {
        this.plugin = plugin;
        playersViewingZones = new HashMap<>();
        playersBlocksViewing = new HashMap<>();
    }

    public void updatePlayerZones(Player player, ArrayList<ZonePvP> nearbyZones) {
        ArrayList<ZonePvP> currentZones = playersViewingZones.get(player);
        //Ver si ya esta viendo alguna zona
        if (currentZones != null) {
            // Si no hay zonas cercanas, elimina las zonas actuales
            if (nearbyZones.isEmpty()) {
                removeZoneWalls(player);
                playersViewingZones.remove(player);
                return;
            }

            // Si las zonas que esta viendo son las mismas, se llama al dinamismo
            if (currentZones.equals(nearbyZones)) {
                removeZoneWalls(player);
                addZoneWalls(player, nearbyZones);
                return;
            }

            // Elimina paredes de zonas que ya no están cerca
            ArrayList<ZonePvP> zonesToRemove = new ArrayList<>(currentZones);
            zonesToRemove.removeAll(nearbyZones);
            removeZoneWalls(player);
        }
        addZoneWalls(player, nearbyZones);
        playersViewingZones.put(player, nearbyZones);
    }

    public void removeZoneWalls(Player player) {
        if (playersBlocksViewing.get(player) != null) {
            ArrayList<Location> blocksToRemove = new ArrayList<>(playersBlocksViewing.get(player));
            // Mantener solo los bloques que no deberían seguir viéndose
            PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
            if(playerData.isProtected()){
                Set<Location> validLocations = new HashSet<>(this.getPlayerCuboId(player).getLocationsIn());
                blocksToRemove.removeAll(validLocations);
            }
            for (Location wallBlock : blocksToRemove) {
                plugin.getProtocolLibHook().removeBlockUpdate(player, wallBlock);
            }
            // Actualizar la lista de bloques vistos por el jugador
            playersBlocksViewing.get(player).removeAll(blocksToRemove);
            if (playersBlocksViewing.get(player).isEmpty()) {
                playersBlocksViewing.remove(player);
            }
        }
    }

    public void addZoneWalls(Player player, ArrayList<ZonePvP> zones) {
        Set<Location> validLocations = new HashSet<>(this.getPlayerCuboId(player).getLocationsIn());
        ArrayList<Location> viewedBlocks = playersBlocksViewing.computeIfAbsent(player, k -> new ArrayList<>());
        for (ZonePvP zone : zones) {
            for (Location wallBlock : zone.getWalls()) {
                if (validLocations.contains(wallBlock) &&
                        wallBlock.getWorld() != null &&
                        wallBlock.getWorld().getBlockAt(wallBlock).getType() == Material.AIR) {
                    plugin.getProtocolLibHook().sendBlockUpdate(player, wallBlock);
                    viewedBlocks.add(wallBlock);
                }
            }
        }
        playersBlocksViewing.put(player, viewedBlocks);
    }

    public Cuboid getPlayerCuboId(Player player){
        Location location = player.getLocation();
        int distance = plugin.getConfigManager().getDistanceZonePvP()+1;
        Location corner1 = new Location(location.getWorld(),
                location.getBlockX()-distance,
                location.getBlockY()-distance,
                location.getBlockZ()-distance);
        Location corner2 = new Location(location.getWorld(),
                location.getBlockX()+distance,
                location.getBlockY()+distance,
                location.getBlockZ()+distance);
        return new Cuboid(corner1, corner2);
    }

    public void removeFullPlayer(Player player){
        playersViewingZones.remove(player);
        playersBlocksViewing.remove(player);
    }

    public void removeAllZoneWallsToAllPlayers(){
        for(Player p : Bukkit.getOnlinePlayers()) {
            if (playersBlocksViewing.get(p) != null) {
                ArrayList<Location> blocksToRemove = new ArrayList<>(playersBlocksViewing.get(p));
                for (Location wallBlock : blocksToRemove) {
                    plugin.getProtocolLibHook().removeBlockUpdate(p, wallBlock);
                }
            }
            removeFullPlayer(p);
        }
    }

    public void removeZoneWallsToAllPlayers(ZonePvP zone){
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(plugin.getZonesManager().getCuboIdDistance(zone).isInWithoutY(p.getLocation())){
                if (playersBlocksViewing.get(p) != null) {
                    Set<Location> blocksToRemove = new HashSet<>(playersBlocksViewing.get(p));
                    blocksToRemove.retainAll(zone.getWalls());
                    for (Location wallBlock : blocksToRemove) {
                        plugin.getProtocolLibHook().removeBlockUpdate(p, wallBlock);
                    }
                }
            }
        }
    }

    public boolean isViewingBlock(Player player, Location location){
        return playersBlocksViewing.get(player).contains(location);
    }
}
