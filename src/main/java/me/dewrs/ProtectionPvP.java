package me.dewrs;

import me.dewrs.Config.ConfigManager;
import me.dewrs.Config.MessagesManager;
import me.dewrs.Config.UserDataManager;
import me.dewrs.Config.ZoneDataManager;
import me.dewrs.Events.PlayerListener;
import me.dewrs.Events.WandListener;
import me.dewrs.Managers.PlayerDataManager;
import me.dewrs.Managers.SafeZoneManager;
import me.dewrs.Managers.ZoneViewerManager;
import me.dewrs.Managers.ZonesManager;
import me.dewrs.Model.PlayerData;
import me.dewrs.Packets.ProtocolLibHook;
import me.dewrs.Utils.ColoredMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class ProtectionPvP extends JavaPlugin {
    public static String prefix = ColoredMessage.setColor("&8[&eProtectionPvP&8]&r ");
    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private UserDataManager userDataManager;
    private PlayerDataManager playerDataManager;
    private ZonesManager zonesManager;
    private ZoneDataManager zoneDataManager;
    private ZoneViewerManager zoneViewerManager;
    private ProtocolLibHook protocolLibHook;
    private SafeZoneManager safeZoneManager;
    public static HashMap<Player, Location> cacheLocCorner1;
    public static HashMap<Player, Location> cacheLocCorner2;

    public void onEnable(){
        zonesManager = new ZonesManager(this);
        zoneDataManager = new ZoneDataManager(this);
        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        playerDataManager = new PlayerDataManager(this);
        userDataManager = new UserDataManager(this, "players");
        protocolLibHook = new ProtocolLibHook(this);
        zoneViewerManager = new ZoneViewerManager(this);
        safeZoneManager = new SafeZoneManager(this);
        protocolLibHook.addBlockDigInterception();
        protocolLibHook.addBlockInteractInterception();
        regCommands();
        regEvents();
        cacheLocCorner1 = new HashMap<>();
        cacheLocCorner2 = new HashMap<>();
        Bukkit.getConsoleSender().sendMessage(prefix+ColoredMessage.setColor("has been enabled"));
    }

    public void onDisable(){
        for(PlayerData p : playerDataManager.getPlayers()){
            if(p.isProtected()){
                playerDataManager.setProtectionTime(p, playerDataManager.getNewTimeProtectedPlayer(p));
            }
        }
        Bukkit.getConsoleSender().sendMessage(prefix+ColoredMessage.setColor("has been disabled"));
    }

    public void regCommands(){
        this.getCommand("protectionpvp").setExecutor(new MainCommand(this));
    }

    public void regEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new WandListener(this), this);
    }

    public void reloadPlugin(){
        boolean changeWallsState = configManager.isGlassWallsEnabled();
        boolean changeCanEnterZonesState = configManager.isProtePlayersCanEnterPvPZones();
        this.getMessagesManager().reloadMessages();
        this.getConfigManager().reloadConfig();
        if(!Objects.equals(changeWallsState, configManager.isGlassWallsEnabled())){
            if(!configManager.isGlassWallsEnabled()){
                zoneViewerManager.removeAllZoneWallsToAllPlayers();
            }
        }
        if(!Objects.equals(changeCanEnterZonesState, configManager.isProtePlayersCanEnterPvPZones())){
            if(configManager.isProtePlayersCanEnterPvPZones()){
                zoneViewerManager.removeAllZoneWallsToAllPlayers();
            }
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public UserDataManager getUserDataManager() {
        return userDataManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ZonesManager getZonesManager() {
        return zonesManager;
    }

    public ZoneDataManager getZoneDataManager() {
        return zoneDataManager;
    }

    public ZoneViewerManager getZoneViewerManager() {
        return zoneViewerManager;
    }

    public ProtocolLibHook getProtocolLibHook() {
        return protocolLibHook;
    }

    public SafeZoneManager getSafeZoneManager() {
        return safeZoneManager;
    }
}