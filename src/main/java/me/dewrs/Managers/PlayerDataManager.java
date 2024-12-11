package me.dewrs.Managers;

import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import me.dewrs.Scheduler.TogglePvPTask;
import me.dewrs.Utils.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerDataManager {
    private ProtectionPvP plugin;
    private ArrayList<PlayerData> players;

    public PlayerDataManager(ProtectionPvP plugin) {
        this.plugin = plugin;
    }

    public ArrayList<PlayerData> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerData> players) {
        this.players = players;
    }

    public PlayerData getPlayerData(Player player){
        for(PlayerData playerData : players){
            if(UUID.fromString(playerData.getUuid()).equals(player.getUniqueId())){
                return playerData;
            }
        }
        return null;
    }

    public Player getPlayerByPlayerData(PlayerData playerData){
        return Bukkit.getPlayer(UUID.fromString(playerData.getUuid()));
    }

    public void setCooldownMillis(PlayerData playerData, long cooldown){
        playerData.setCooldownMillis(cooldown);
        plugin.getUserDataManager().writeValuesInConfig(playerData,"cooldown", cooldown);
    }

    public void setProtectionTime(PlayerData playerData, int time){
        playerData.setActualProtectionTime(time);
        plugin.getUserDataManager().writeValuesInConfig(playerData,"time", time);
    }

    public void manageFirstTimeJoin(Player player){
        boolean firstTime = !plugin.getUserDataManager().fileAlreadyRegistered(player.getUniqueId() + ".yml");
        plugin.getUserDataManager().createDataFile(player);
        if (firstTime) {
            PlayerData playerData = new PlayerData(player.getUniqueId().toString());
            playerData.setName(player.getName());
            players.add(playerData);
            plugin.getUserDataManager().writeInitialValues(playerData);
            toggleProtectionPlayer(playerData,true);
            if (plugin.getConfigManager().isGlassWallsEnabled()) {
                ArrayList<ZonePvP> nearbyZones = plugin.getZonesManager().getNearbyZones(player.getLocation());
                plugin.getZoneViewerManager().updatePlayerZones(player, nearbyZones);
            }
            if(!plugin.getConfigManager().isProtePlayersCanEnterPvPZones()){
                plugin.getZonesManager().kickPlayerOfActualZone(player);
            }
        }
    }

    public void updateCooldownMillis(PlayerData playerData){
        long millis = System.currentTimeMillis();
        playerData.setCooldownMillis(millis);
        plugin.getUserDataManager().writeValuesInConfig(playerData,"cooldown", millis);
    }

    public void setProtected(PlayerData playerData, boolean status){
        playerData.setProtected(status);
        plugin.getUserDataManager().writeValuesInConfig(playerData,"isProtected",status);
        if(!status){
            if (plugin.getConfigManager().isGlassWallsEnabled()) {
                Player player = this.getPlayerByPlayerData(playerData);
                plugin.getZoneViewerManager().removeZoneWalls(player);
                plugin.getZoneViewerManager().removeFullPlayer(player);
            }
        }else{
            if(!plugin.getConfigManager().isProtePlayersCanEnterPvPZones()){
                plugin.getZonesManager().kickPlayerOfActualZone(getPlayerByPlayerData(playerData));
            }
        }
    }

    public void toggleProtectionPlayer(PlayerData playerData, boolean status){
        if(status){
            TogglePvPTask togglePvPTask = new TogglePvPTask(plugin,playerData);
            int time = plugin.getConfigManager().getProtectionTime();
            updateCooldownMillis(playerData);
            setProtectionTime(playerData,time);
            setProtected(playerData, true);
            togglePvPTask.runTaskLater(plugin,time*20L);
            playerData.setTaskID(togglePvPTask.getTaskId());
        }else{
            setCooldownMillis(playerData, 0);
            setProtectionTime(playerData, 0);
            setProtected(playerData, false);
        }
    }

    public void reStartProtectionPlayer(PlayerData playerData){
        TogglePvPTask togglePvPTask = new TogglePvPTask(plugin,playerData);
        int time = playerData.getActualProtectionTime();
        updateCooldownMillis(playerData);
        setProtected(playerData, true);
        togglePvPTask.runTaskLater(plugin,time*20L);
        playerData.setTaskID(togglePvPTask.getTaskId());
    }

    public String getCountdown(PlayerData playerData){
        return Countdown.getCountdown(playerData.getCooldownMillis(), playerData.getActualProtectionTime());
    }

    public int getNewTimeProtectedPlayer(PlayerData playerData){
        return Countdown.getSecsLeft(playerData.getCooldownMillis(), playerData.getActualProtectionTime());
    }
}
