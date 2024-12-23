package me.dewrs;

import me.dewrs.Api.ExpansionProtectionPvP;
import me.dewrs.Api.ProtectionPvPAPI;
import me.dewrs.Config.ConfigManager;
import me.dewrs.Config.MessagesManager;
import me.dewrs.Config.UserDataManager;
import me.dewrs.Config.ZoneDataManager;
import me.dewrs.Dependencies.Metrics;
import me.dewrs.Events.PlayerListener;
import me.dewrs.Events.WandListener;
import me.dewrs.Managers.*;
import me.dewrs.Model.PlayerData;
import me.dewrs.Packets.ProtocolLibHook;
import me.dewrs.UpdateChecker.UpdateChecker;
import me.dewrs.Utils.ColoredMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public class ProtectionPvP extends JavaPlugin {
    public static String prefix = ColoredMessage.setColor("&8[&eProtectionPvP&8]&r ");
    private PluginDescriptionFile pdfFile = getDescription();
    public String version = pdfFile.getVersion();
    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private UserDataManager userDataManager;
    private PlayerDataManager playerDataManager;
    private ZonesManager zonesManager;
    private ZoneDataManager zoneDataManager;
    private ZoneViewerManager zoneViewerManager;
    private ProtocolLibHook protocolLibHook;
    private SafeZoneManager safeZoneManager;
    private TaskManager taskManager;
    private UpdateChecker updateChecker;
    public static HashMap<Player, Location> cacheLocCorner1;
    public static HashMap<Player, Location> cacheLocCorner2;

    public void onEnable() {
        zonesManager = new ZonesManager(this);
        zoneDataManager = new ZoneDataManager(this);
        configManager = new ConfigManager(this);
        messagesManager = new MessagesManager(this);
        playerDataManager = new PlayerDataManager(this);
        userDataManager = new UserDataManager(this, "players");
        protocolLibHook = new ProtocolLibHook(this);
        zoneViewerManager = new ZoneViewerManager(this);
        safeZoneManager = new SafeZoneManager(this);
        taskManager = new TaskManager(this);
        protocolLibHook.addBlockDigInterception();
        if (isPaperServer()) {
            protocolLibHook.addBlockInteractInterceptionPaper();
        } else {
            protocolLibHook.addBlockInteractInterceptionSpigot();
        }
        ProtectionPvPAPI api = new ProtectionPvPAPI(this);
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new ExpansionProtectionPvP(this).register();
        }
        regCommands();
        regEvents();
        cacheLocCorner1 = new HashMap<>();
        cacheLocCorner2 = new HashMap<>();
        Metrics metrics = new Metrics(this,24224);
        Bukkit.getConsoleSender().sendMessage(prefix + ColoredMessage.setColor("&aHas been enabled"));
        Bukkit.getConsoleSender().sendMessage(prefix + ColoredMessage.setColor("&aPlugin created by &edewrs"));
        updateChecker = new UpdateChecker(version);
        manageUpdateChecker();
    }

    public void onDisable() {
        for (PlayerData p : playerDataManager.getPlayers()) {
            if (p.isProtected()) {
                playerDataManager.setProtectionTime(p, playerDataManager.getNewTimeProtectedPlayer(p));
            }
        }
        Bukkit.getConsoleSender().sendMessage(prefix + ColoredMessage.setColor("&cHas been disabled"));
        Bukkit.getConsoleSender().sendMessage(prefix + ColoredMessage.setColor("&aThanks for using me!"));
    }

    private void regCommands() {
        this.getCommand("protectionpvp").setExecutor(new MainCommand(this));
    }

    private void regEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new WandListener(this), this);
    }

    public void reloadPlugin() {
        boolean changeWallsState = configManager.isGlassWallsEnabled();
        boolean changeCanEnterZonesState = configManager.isProtePlayersCanEnterPvPZones();
        this.getMessagesManager().reloadMessages();
        this.getConfigManager().reloadConfig();
        if (!Objects.equals(changeWallsState, configManager.isGlassWallsEnabled())) {
            if (!configManager.isGlassWallsEnabled()) {
                zoneViewerManager.removeAllZoneWallsToAllPlayers();
            }
        }
        if (!Objects.equals(changeCanEnterZonesState, configManager.isProtePlayersCanEnterPvPZones())) {
            if (configManager.isProtePlayersCanEnterPvPZones()) {
                zoneViewerManager.removeAllZoneWallsToAllPlayers();
            }
        }
    }

    private boolean isPaperServer(){
        boolean isPaper = false;
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {
        }
        return isPaper;
    }

    private void manageUpdateChecker(){
        if (!updateChecker.check().isError()) {
            String latestVersion = updateChecker.check().getLatestVersion();
            if (latestVersion != null) {
                Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + "*********************************************************************"));
                Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + "&cProtectionPvP is outdated!"));
                Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + "&cNewest version: &e"+latestVersion));
                Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + "&cYour version: &e"+version));
                Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + "&cPlease Update Here: &ehttps://spigotmc.org/resources/121277"));
                Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + "*********************************************************************"));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ColoredMessage.setColor(prefix + " &cError while checking update."));
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

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}