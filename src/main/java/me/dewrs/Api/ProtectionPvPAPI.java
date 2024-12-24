package me.dewrs.Api;

import me.dewrs.Managers.ZonesManager;
import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ProtectionPvPAPI {
    private static ProtectionPvP plugin;
    public ProtectionPvPAPI(ProtectionPvP plugin){
        this.plugin = plugin;
    }

    public static int getActualProtectionTime(Player player){
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        return plugin.getPlayerDataManager().getNewTimeProtectedPlayer(playerData);
    }

    public static boolean isProtected(Player player){
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        return playerData.isProtected();
    }

    public static List<String> getPvPZones(){
        ZonesManager zonesManager = plugin.getZonesManager();
        List<String> listString = new ArrayList<>();
        for (ZonePvP zone : zonesManager.getZones()) {
            listString.add(zone.getName());
        }
        return listString;
    }
}
