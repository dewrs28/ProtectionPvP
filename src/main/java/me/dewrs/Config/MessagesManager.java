package me.dewrs.Config;

import me.dewrs.ProtectionPvP;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MessagesManager {
    private ProtectionPvP plugin;
    private CustomConfig customConfig;
    private List<String> helpPlayer;
    private List<String>  helpAdmin;
    private String incorrectCommand;
    private String proteOffYourself;
    private String noPermission;
    private String playerOffline;
    private String proteAlreadyOff;
    private String proteStatus;
    private String reload;
    private String proteStatusOther;
    private String proteAlreadyOnOther;
    private String proteAlreadyOffOther;
    private String proteOnOther;
    private String proteOffOther;
    private String proteOnYourself;
    private String victimProteOn;
    private String damagerProteOn;
    private String wandGive;
    private String wandSel;
    private String zoneCreate;
    private String zoneNoSelected;
    private String zoneNoName;
    private String zoneNameExist;
    private String zoneList;
    private String zoneDelete;
    private String zoneNoExist;
    private String zoneRename;
    private String zoneListEmpty;
    private String zoneSetTp;
    private String tpZoneProteOn;
    private String zoneOverlap;
    private String proteAlert;
    public MessagesManager(ProtectionPvP plugin){
        this.plugin = plugin;
        customConfig = new CustomConfig("messages.yml", null, plugin);
        customConfig.registerConfig();
        loadMessages();
    }

    private void loadMessages(){
        FileConfiguration config = customConfig.getConfig();
        helpPlayer = config.getStringList("help_message_player");
        helpAdmin = config.getStringList("help_message_admin");
        incorrectCommand = config.getString("messages.incorrect_command");
        proteOffYourself = config.getString("messages.prote_off_yourself");
        noPermission = config.getString("messages.no_permission");
        playerOffline = config.getString("messages.player_offline");
        proteAlreadyOff = config.getString("messages.prote_already_off");
        proteStatus = config.getString("messages.prote_status");
        reload = config.getString("messages.reload");
        proteStatusOther = config.getString("messages.prote_status_other");
        proteAlreadyOffOther = config.getString("messages.prote_already_off_other");
        proteAlreadyOnOther = config.getString("messages.prote_already_on_other");
        proteOffOther = config.getString("messages.prote_off_other");
        proteOnOther = config.getString("messages.prote_on_other");
        proteOnYourself = config.getString("messages.prote_on_yourself");
        victimProteOn = config.getString("messages.victim_prote_on");
        damagerProteOn = config.getString("messages.damager_prote_on");
        wandGive = config.getString("messages.wand_give");
        wandSel = config.getString("messages.wand_sel");
        zoneCreate = config.getString("messages.zone_create");
        zoneNoSelected = config.getString("messages.zone_no_selected");
        zoneNoName = config.getString("messages.zone_no_name");
        zoneNameExist = config.getString("messages.zone_name_exist");
        zoneList = config.getString("messages.zone_list");
        zoneDelete = config.getString("messages.zone_delete");
        zoneNoExist = config.getString("messages.zone_no_exist");
        zoneRename = config.getString("messages.zone_rename");
        zoneListEmpty = config.getString("messages.zone_list_empty");
        zoneSetTp = config.getString("messages.zone_tp_set");
        tpZoneProteOn = config.getString("messages.tp_zone_prote_on");
        zoneOverlap = config.getString("messages.zone_overlap");
        proteAlert = config.getString("messages.prote_alert");
    }

    public void reloadMessages(){
        customConfig.reloadConfig();
        loadMessages();
    }

    public List<String> getHelpPlayer() {
        return helpPlayer;
    }

    public List<String> getHelpAdmin() {
        return helpAdmin;
    }

    public String getIncorrectCommand() {
        return incorrectCommand;
    }

    public String getNoPermission() {
        return noPermission;
    }

    public String getPlayerOffline() {
        return playerOffline;
    }

    public String getReload() {
        return reload;
    }

    public String getWandGive() {
        return wandGive;
    }

    public String getWandSel() {
        return wandSel;
    }

    public String getZoneCreate() {
        return zoneCreate;
    }

    public String getZoneNoSelected() {
        return zoneNoSelected;
    }

    public String getZoneNoName() {
        return zoneNoName;
    }

    public String getZoneNameExist() {
        return zoneNameExist;
    }

    public String getZoneList() {
        return zoneList;
    }

    public String getZoneDelete() {
        return zoneDelete;
    }

    public String getZoneNoExist() {
        return zoneNoExist;
    }

    public String getProteOffYourself() {
        return proteOffYourself;
    }

    public String getProteAlreadyOff() {
        return proteAlreadyOff;
    }

    public String getProteStatus() {
        return proteStatus;
    }

    public String getProteStatusOther() {
        return proteStatusOther;
    }

    public String getProteAlreadyOnOther() {
        return proteAlreadyOnOther;
    }

    public String getProteAlreadyOffOther() {
        return proteAlreadyOffOther;
    }

    public String getProteOnOther() {
        return proteOnOther;
    }

    public String getProteOffOther() {
        return proteOffOther;
    }

    public String getProteOnYourself() {
        return proteOnYourself;
    }

    public String getVictimProteOn() {
        return victimProteOn;
    }

    public String getDamagerProteOn() {
        return damagerProteOn;
    }

    public String getZoneRename() {
        return zoneRename;
    }

    public String getZoneListEmpty() {
        return zoneListEmpty;
    }

    public String getZoneSetTp() {
        return zoneSetTp;
    }

    public String getTpZoneProteOn() {
        return tpZoneProteOn;
    }

    public String getZoneOverlap() {
        return zoneOverlap;
    }

    public String getProteAlert() {
        return proteAlert;
    }
}