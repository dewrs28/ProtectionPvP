package me.dewrs.Config;

import me.dewrs.Managers.PlayerDataManager;
import me.dewrs.Model.PlayerData;
import me.dewrs.ProtectionPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDataManager {
    private ProtectionPvP plugin;
    private ArrayList<CustomConfig> configFiles;
    private String folderName;

    public UserDataManager(ProtectionPvP plugin, String folderName) {
        this.plugin = plugin;
        this.folderName = folderName;
        this.configFiles = new ArrayList<>();
        createFolder();
        reloadConfigs();
        fixPlayerData();
        configurePlayerDatas();
    }

    public void reloadConfigs() {
        this.configFiles = new ArrayList<>();
        registerConfigFiles();
    }

    private void createFolder() {
        File folder;
        try {
            folder = new File(plugin.getDataFolder() + File.separator + folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (SecurityException e) {
            folder = null;
        }
    }

    public void createDataFile(Player player) {
        File file = new File(plugin.getDataFolder() + File.separator + folderName + File.separator + player.getUniqueId() + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                registerConfigFile(file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveConfigFiles() {
        for (CustomConfig configFile : configFiles) {
            configFile.saveConfig();
        }
    }

    private void registerConfigFiles() {
        String path = plugin.getDataFolder() + File.separator + folderName;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String pathName = listOfFile.getName();
                CustomConfig config = new CustomConfig(pathName, folderName, plugin);
                config.registerConfig();
                configFiles.add(config);
            }
        }
    }

    public ArrayList<CustomConfig> getConfigs() {
        return this.configFiles;
    }

    public boolean fileAlreadyRegistered(String pathName) {
        for (CustomConfig configFile : configFiles) {
            if (configFile.getPath().equals(pathName)) {
                return true;
            }
        }
        return false;
    }

    private void configurePlayerDatas(){
        ArrayList<PlayerData> players = new ArrayList<>();
        for(CustomConfig customConfig : configFiles){
            if(isValidPlayerData(customConfig.getPath())){
                String uuid = customConfig.getPath().replace(".yml", "");
                PlayerData playerData = new PlayerData(uuid);
                FileConfiguration config = customConfig.getConfig();
                playerData.setName(config.getString("name"));
                playerData.setCooldownMillis(config.getLong("cooldown"));
                playerData.setActualProtectionTime(config.getInt("time"));
                playerData.setProtected(config.getBoolean("isProtected"));
                playerData.setListAlertTimes((ArrayList<Integer>) config.getIntegerList("alertTimes"));
                players.add(playerData);
            }
        }
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        playerDataManager.setPlayers(players);
    }

    public CustomConfig getConfigFile(String pathName) {
        for (CustomConfig configFile : configFiles) {
            if (configFile.getPath().equals(pathName)) {
                return configFile;
            }
        }
        return null;
    }

    private void registerConfigFile(String pathName) {
        if (!fileAlreadyRegistered(pathName)) {
            CustomConfig config = new CustomConfig(pathName, folderName, plugin);
            config.registerConfig();
            configFiles.add(config);
        }
    }

    private void saveConfig(PlayerData playerData) {
        CustomConfig playerConfig = getConfigFile(playerData.getUuid() + ".yml");
        if (playerConfig == null) {
            registerConfigFile(playerData.getUuid() + ".yml");
            playerConfig = getConfigFile(playerData.getUuid() + ".yml");
        }
        playerConfig.saveConfig();
    }

    private FileConfiguration getFileConfiguration(PlayerData playerData) {
        return this.getConfigFile(playerData.getUuid() + ".yml").getConfig();
    }

    public void writeInitialValues(PlayerData playerData){
        if(fileAlreadyRegistered(playerData.getUuid()+".yml")) {
            if(playerData.getName() != null) {
                this.getFileConfiguration(playerData).set("name", playerData.getName());
            }
            this.getFileConfiguration(playerData).set("cooldown", 0);
            this.getFileConfiguration(playerData).set("time", 0);
            this.getFileConfiguration(playerData).set("isProtected", false);
            this.getFileConfiguration(playerData).set("alertTimes", new ArrayList<Integer>());
            this.saveConfig(playerData);
        }
    }

    public void writeValuesInConfig(PlayerData playerData, String path, Object value){
        if(fileAlreadyRegistered(playerData.getUuid()+".yml")) {
            this.getFileConfiguration(playerData).set(path, value);
            this.saveConfig(playerData);
        }
    }

    public boolean isValidPlayerData(String pathName){
        CustomConfig customConfig = getConfigFile(pathName);
        FileConfiguration config = customConfig.getConfig();
        List<String> paths = Arrays.asList("name", "cooldown", "time", "isProtected");
        boolean valid = true;
        for(String path : paths){
            if(!config.contains(path)){
                valid = false;
                break;
            }
        }
        return valid;
    }

    private void fixPlayerData(){
        for(CustomConfig customConfig : configFiles){
            if(!isValidPlayerData(customConfig.getPath())){
                String uuid = customConfig.getPath().replace(".yml", "");
                PlayerData playerData = new PlayerData(uuid);
                writeInitialValues(playerData);
            }
        }
    }
}
