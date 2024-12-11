package me.dewrs.Config;

import me.dewrs.ProtectionPvP;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    private ProtectionPvP plugin;
    private CustomConfig customConfig;
    private int protectionTime;
    private String nameWand;
    private List<String> loreWand;
    private int distanceZonePvP;
    private boolean glassWallsEnabled;
    private boolean protePlayersCanEnterPvPZones;
    public ConfigManager(ProtectionPvP plugin){
        this.plugin = plugin;
        customConfig = new CustomConfig("config.yml", null, plugin);
        customConfig.registerConfig();
        loadConfig();
    }

    public void loadConfig(){
        FileConfiguration config = customConfig.getConfig();
        protectionTime = config.getInt("settings.time_protection");
        nameWand = config.getString("wand_selector.name");
        loreWand = config.getStringList("wand_selector.lore");
        distanceZonePvP = config.getInt("settings.block_distance_zone_pvp");
        glassWallsEnabled = config.getBoolean("settings.glass_walls_enabled");
        protePlayersCanEnterPvPZones = config.getBoolean("settings.prote_players_can_enter_pvp_zones");
    }

    public void reloadConfig(){
        customConfig.reloadConfig();
        loadConfig();
    }

    public int getProtectionTime() {
        return protectionTime;
    }

    public String getNameWand() {
        return nameWand;
    }

    public List<String> getLoreWand() {
        return loreWand;
    }

    public int getDistanceZonePvP() {
        return distanceZonePvP;
    }

    public boolean isGlassWallsEnabled() {
        return glassWallsEnabled;
    }

    public boolean isProtePlayersCanEnterPvPZones() {
        return protePlayersCanEnterPvPZones;
    }
}
