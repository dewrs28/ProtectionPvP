package me.dewrs.Managers;

import me.dewrs.Api.ToggleProtectionEvent;
import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
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

    public void setListAlertTimes(PlayerData playerData, ArrayList<Integer> list){
        playerData.setListAlertTimes(list);
        plugin.getUserDataManager().writeValuesInConfig(playerData, "alertTimes", list);
    }

    public void manageFirstTimeJoin(Player player){
        boolean firstTime = !plugin.getUserDataManager().fileAlreadyRegistered(player.getUniqueId() + ".yml");
        plugin.getUserDataManager().createDataFile(player);
        if (firstTime) {
            PlayerData playerData = new PlayerData(player.getUniqueId().toString());
            playerData.setName(player.getName());
            players.add(playerData);
            plugin.getUserDataManager().writeInitialValues(playerData);
            if(plugin.getConfigManager().isProteOnFirstJoin()) {
                toggleProtectionPlayer(playerData, true);
                if (plugin.getConfigManager().isGlassWallsEnabled()) {
                    ArrayList<ZonePvP> nearbyZones = plugin.getZonesManager().getNearbyZones(player.getLocation());
                    plugin.getZoneViewerManager().updatePlayerZones(player, nearbyZones);
                }
                if (!plugin.getConfigManager().isProtePlayersCanEnterPvPZones()) {
                    plugin.getZonesManager().kickPlayerOfActualZone(player);
                }
                return;
            }
            toggleProtectionPlayer(playerData, false);
        }
    }

    private void updateCooldownMillis(PlayerData playerData){
        long millis = System.currentTimeMillis();
        playerData.setCooldownMillis(millis);
        plugin.getUserDataManager().writeValuesInConfig(playerData,"cooldown", millis);
    }

    private void setProtected(PlayerData playerData, boolean status){
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
            int time = plugin.getConfigManager().getProtectionTime();
            updateCooldownMillis(playerData);
            setProtectionTime(playerData,time);
            setProtected(playerData, true);
            setListAlertTimes(playerData, getAlertTimes(playerData));
            plugin.getTaskManager().startTaskProteOn(playerData, time, false);
            Player player = getPlayerByPlayerData(playerData);
            Bukkit.getPluginManager().callEvent(new ToggleProtectionEvent(player, true));
        }else{
            setCooldownMillis(playerData, 0);
            setProtectionTime(playerData, 0);
            setProtected(playerData, false);
            setListAlertTimes(playerData, new ArrayList<>());
            Player player = getPlayerByPlayerData(playerData);
            Bukkit.getPluginManager().callEvent(new ToggleProtectionEvent(player, false));
        }
    }

    public void reStartProtectionPlayer(PlayerData playerData){
        int time = playerData.getActualProtectionTime();
        updateCooldownMillis(playerData);
        setProtected(playerData, true);
        plugin.getTaskManager().startTaskProteOn(playerData, time, true);
    }

    public String getCountdown(PlayerData playerData){
        return Countdown.getCountdown(playerData.getCooldownMillis(), playerData.getActualProtectionTime());
    }

    public int getNewTimeProtectedPlayer(PlayerData playerData){
        return Countdown.getSecsLeft(playerData.getCooldownMillis(), playerData.getActualProtectionTime());
    }

    public ArrayList<Integer> getAlertTimes(PlayerData playerData) {
        ArrayList<Integer> list = new ArrayList<>();
        int protectedTime = playerData.getActualProtectionTime();
        int interval = plugin.getConfigManager().getTimeToAlertProte();

        for (int time = protectedTime - interval; time > 0; time -= interval) {
            list.add(time);
        }
        if(plugin.getConfigManager().isOneMinAlertEnabled()){
            if(!list.contains(60)){
                list.add(60);
            }
        }
        return list;
    }
}
