package me.dewrs.Config;

import me.dewrs.Managers.ZonesManager;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class ZoneDataManager {
    private ProtectionPvP plugin;
    private CustomConfig customConfig;
    public ZoneDataManager(ProtectionPvP plugin){
        this.plugin = plugin;
        customConfig = new CustomConfig("zones.yml", null, plugin);
        customConfig.registerConfig();
        loadZones();
    }

    public void loadZones(){
        ArrayList<ZonePvP> zones = new ArrayList<>();
        if(getFileConfiguration().getConfigurationSection("pvp_zones") != null) {
            for (String key : getFileConfiguration().getConfigurationSection("pvp_zones").getKeys(false)) {
                String world = getFileConfiguration().getString("pvp_zones." + key + ".world");
                if(world != null) {
                    Location corner1 = new Location(Bukkit.getWorld(world),
                            getFileConfiguration().getInt("pvp_zones."+key+".corner_1.x"),
                            50,
                            getFileConfiguration().getInt("pvp_zones."+key+".corner_1.z"));
                    Location corner2 = new Location(Bukkit.getWorld(world),
                            getFileConfiguration().getInt("pvp_zones."+key+".corner_2.x"),
                            50,
                            getFileConfiguration().getInt("pvp_zones."+key+".corner_2.z"));
                    ZonePvP zonePvP = new ZonePvP(key, world, corner1, corner2);
                    if(getFileConfiguration().contains("pvp.zones."+key+".tp")){
                        double x = getFileConfiguration().getDouble("pvp_zones."+key+".tp.x");
                        double y = getFileConfiguration().getDouble("pvp_zones."+key+".tp.y");
                        double z = getFileConfiguration().getDouble("pvp_zones."+key+".tp.z");
                        double yaw = (float) getFileConfiguration().getDouble("pvp_zones."+key+".tp.yaw");
                        double pitch = (float) getFileConfiguration().getDouble("pvp_zones."+key+".tp.pitch");
                        Location location = new Location(Bukkit.getWorld(world), x, y, z, (float) yaw, (float) pitch);
                        zonePvP.setTp(location);
                    }
                    zones.add(zonePvP);
                }
            }
        }
        ZonesManager zonesManager = plugin.getZonesManager();
        zonesManager.setZones(zones);
    }

    public FileConfiguration getFileConfiguration(){
        return customConfig.getConfig();
    }

    public void saveConfig(){
        this.customConfig.saveConfig();
    }

    public void writeNewZoneInConfig(ZonePvP zone){
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".world", zone.getWorld());

        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".corner_1."+"x", zone.getCorner1().getBlockX());
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".corner_1."+"z", zone.getCorner1().getBlockZ());

        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".corner_2."+"x", zone.getCorner2().getBlockX());
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".corner_2."+"z", zone.getCorner2().getBlockZ());

        this.saveConfig();
    }

    public void renameZoneInConfig(ZonePvP zone, String newName){
        ConfigurationSection configurationSection = this.getFileConfiguration().getConfigurationSection("pvp_zones."+zone.getName());
        if(configurationSection != null) {
            this.deleteZoneInConfig(zone);
            this.getFileConfiguration().set("pvp_zones."+newName, configurationSection);
            this.saveConfig();
        }
    }

    public void writeTpZoneInConfig(ZonePvP zone, Location location){
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".tp.x", x);
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".tp.y", y);
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".tp.z", z);
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".tp.yaw", yaw);
        this.getFileConfiguration().set("pvp_zones."+zone.getName()+".tp.pitch", pitch);

        this.saveConfig();
    }

    public void deleteZoneInConfig(ZonePvP zone){
        this.getFileConfiguration().set("pvp_zones."+zone.getName(), null);
        this.saveConfig();
    }

}
