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
    private boolean proteOnFirstJoin;
    private boolean isTitleProteOnEnabled;
    private boolean isTitleProteOffEnabled;
    private boolean isTitleProteAlertEnabled;
    private String titleProteOnUp;
    private String titleProteOnDown;
    private String titleProteOffUp;
    private String titleProteOffDown;
    private String titleProteAlertUp;
    private String titleProteAlertDown;
    private boolean isSoundProteOnEnabled;
    private boolean isSoundProteOffEnabled;
    private boolean isSoundProteAlertEnabled;
    private String soundProteOn;
    private String soundProteOff;
    private String soundProteAlert;
    private int timeToAlertProte;
    private boolean oneMinAlertEnabled;
    public ConfigManager(ProtectionPvP plugin){
        this.plugin = plugin;
        customConfig = new CustomConfig("config.yml", null, plugin);
        customConfig.registerConfig();
        loadConfig();
    }

    private void loadConfig(){
        FileConfiguration config = customConfig.getConfig();
        protectionTime = config.getInt("general_settings.time_protection");
        nameWand = config.getString("wand_selector_settings.name");
        loreWand = config.getStringList("wand_selector_settings.lore");
        distanceZonePvP = config.getInt("zones_settings.block_distance_to_see_walls");
        glassWallsEnabled = config.getBoolean("zones_settings.glass_walls_enabled");
        protePlayersCanEnterPvPZones = config.getBoolean("zones_settings.prote_players_can_enter_pvp_zones");
        proteOnFirstJoin = config.getBoolean("general_settings.protection_on_first_join");
        isTitleProteOnEnabled = config.getBoolean("titles_settings.prote_on.enabled");
        isTitleProteOffEnabled = config.getBoolean("titles_settings.prote_off.enabled");
        isTitleProteAlertEnabled = config.getBoolean("titles_settings.prote_alert.enabled");
        titleProteOffUp = config.getString("titles_settings.prote_off.up");
        titleProteOffDown = config.getString("titles_settings.prote_off.down");
        titleProteOnUp = config.getString("titles_settings.prote_on.up");
        titleProteOnDown = config.getString("titles_settings.prote_on.down");
        titleProteAlertUp = config.getString("titles_settings.prote_alert.up");
        titleProteAlertDown = config.getString("titles_settings.prote_alert.down");
        isSoundProteOffEnabled = config.getBoolean("sounds_settings.prote_off.enabled");
        isSoundProteOnEnabled = config.getBoolean("sounds_settings.prote_on.enabled");
        isSoundProteAlertEnabled = config.getBoolean("sounds_settings.prote_alert.enabled");
        soundProteOff = config.getString("sounds_settings.prote_off.sound");
        soundProteOn = config.getString("sounds_settings.prote_on.sound");
        soundProteAlert = config.getString("sounds_settings.prote_alert.sound");
        timeToAlertProte = config.getInt("general_settings.time_to_protection_alert");
        oneMinAlertEnabled = config.getBoolean("general_settings.one_minute_alert_enabled");
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

    public boolean isProteOnFirstJoin() {
        return proteOnFirstJoin;
    }

    public boolean isTitleProteOnEnabled() {
        return isTitleProteOnEnabled;
    }

    public boolean isTitleProteOffEnabled() {
        return isTitleProteOffEnabled;
    }

    public String getTitleProteOnUp() {
        return titleProteOnUp;
    }

    public String getTitleProteOnDown() {
        return titleProteOnDown;
    }

    public String getTitleProteOffUp() {
        return titleProteOffUp;
    }

    public String getTitleProteOffDown() {
        return titleProteOffDown;
    }

    public boolean isSoundProteOnEnabled() {
        return isSoundProteOnEnabled;
    }

    public boolean isSoundProteOffEnabled() {
        return isSoundProteOffEnabled;
    }

    public String getSoundProteOn() {
        return soundProteOn;
    }

    public String getSoundProteOff() {
        return soundProteOff;
    }

    public int getTimeToAlertProte() {
        return timeToAlertProte;
    }

    public boolean isTitleProteAlertEnabled() {
        return isTitleProteAlertEnabled;
    }

    public String getTitleProteAlertUp() {
        return titleProteAlertUp;
    }

    public String getTitleProteAlertDown() {
        return titleProteAlertDown;
    }

    public boolean isSoundProteAlertEnabled() {
        return isSoundProteAlertEnabled;
    }

    public String getSoundProteAlert() {
        return soundProteAlert;
    }

    public boolean isOneMinAlertEnabled() {
        return oneMinAlertEnabled;
    }
}
