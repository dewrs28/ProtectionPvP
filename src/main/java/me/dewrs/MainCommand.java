package me.dewrs;

import me.dewrs.Enums.CustomSound;
import me.dewrs.Enums.CustomTitle;
import me.dewrs.Managers.PlayerDataManager;
import me.dewrs.Managers.ZonesManager;
import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.Utils.ColoredMessage;
import me.dewrs.Utils.ItemStackUtils;
import me.dewrs.Utils.SoundUtils;
import me.dewrs.Utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MainCommand implements CommandExecutor {
    private ProtectionPvP plugin;

    public MainCommand(ProtectionPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ProtectionPvP.prefix+ ColoredMessage.setColor("&cOnly Players"));
            return true;
        }
        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("off")){
                subCommandProteOff(sender);
                return true;
            }
            if(args[0].equalsIgnoreCase("status")){
                subCommandStatus(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("set")){
                subCommandSet(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("createzone")){
                subCommandCreateZone(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("deletezone")){
                subCommandDeleteZone(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("renamezone")){
                subCommandRenameZone(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("settp")){
                subCommandSetTpZone(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("wand")){
                subCommandWand(sender);
                return true;
            }
            if(args[0].equalsIgnoreCase("listzones")){
                subCommandListZones(sender);
                return true;
            }
            if(args[0].equalsIgnoreCase("help")){
                subCommandHelp(sender, args);
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                subCommandReload(sender);
                return true;
            }
        }
        sender.sendMessage(ColoredMessage.setColor(ProtectionPvP.prefix+plugin.getMessagesManager().getIncorrectCommand()));
        return true;
    }

    public void subCommandSet(CommandSender sender, String[] args){
        // /pvp set player true/false
        //      args0 args1 args2
        if(!sender.hasPermission("protectionpvp.admin.set")){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        if(args.length != 3){
            sender.sendMessage(ColoredMessage.setColor(ProtectionPvP.prefix+plugin.getMessagesManager().getIncorrectCommand()));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if(target == null){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getPlayerOffline()));
            return;
        }
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        PlayerData playerData = playerDataManager.getPlayerData(target);
        String booleanSet = args[2];
        if (booleanSet.equalsIgnoreCase("off")) {
            if(playerData.isProtected()) {
                playerDataManager.toggleProtectionPlayer(playerData, false);
                plugin.getTaskManager().stopAllTask(playerData);
                sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteOffOther().replaceAll("%p%", playerData.getName())));
                target.sendMessage(ColoredMessage.setColor(plugin.getMessagesManager().getProteOffYourself()));
                if(plugin.getConfigManager().isTitleProteOffEnabled()) {
                    TitleUtils.sendTitle(target, CustomTitle.PROTE_OFF, plugin);
                }
                if(plugin.getConfigManager().isSoundProteOffEnabled()) {
                    SoundUtils.sendSound(target, CustomSound.PROTE_OFF, plugin);
                }
                return;
            }
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteAlreadyOffOther().replaceAll("%p%", playerData.getName())));
        }else if(booleanSet.equalsIgnoreCase("on")){
            if(!playerData.isProtected()) {
                playerDataManager.toggleProtectionPlayer(playerData, true);
                sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteOnOther().replaceAll("%p%", playerData.getName())));
                target.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteOnYourself()));
                if(plugin.getConfigManager().isTitleProteOnEnabled()) {
                    TitleUtils.sendTitle(target, CustomTitle.PROTE_ON, plugin);
                }
                if(plugin.getConfigManager().isSoundProteOnEnabled()) {
                    SoundUtils.sendSound(target, CustomSound.PROTE_ON, plugin);
                }
                return;
            }
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteAlreadyOnOther().replaceAll("%p%", playerData.getName())));
        }else{
            sender.sendMessage(ColoredMessage.setColor(ProtectionPvP.prefix+plugin.getMessagesManager().getIncorrectCommand()));
        }
    }

    public void subCommandHelp(CommandSender sender, String[] args){
        if(args.length == 1){
            help(sender, 1);
            return;
        }
        String relative = args[1];
        if(relative.equalsIgnoreCase("admin")){
            if(sender.hasPermission("protectionpvp.admin")) {
                help(sender, 2);
                return;
            }
            sender.sendMessage(ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
        }

    }

    public void subCommandStatus(CommandSender sender, String[] args){
        // /pvp status (nick)
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        PlayerData playerData;
        //Si esta viendo su propio status
        if(args.length == 1){
            playerData = playerDataManager.getPlayerData((Player) sender);
            if(playerData.isProtected()){
                String countdown = playerDataManager.getCountdown(playerData);
                sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteStatus().replaceAll("%m%", countdown)));
                return;
            }
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteAlreadyOff()));
            return;
        }
        //Si esta viendo el status de otro
        Player player = Bukkit.getPlayerExact(args[1]);
        if(player == null){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getPlayerOffline()));
            return;
        }
        playerData = playerDataManager.getPlayerData(player);
        if(playerData.isProtected()){
            String countdown = playerDataManager.getCountdown(playerData);
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteStatusOther().replaceAll("%m%", countdown)
                    .replaceAll("%p%", playerData.getName())));
            return;
        }
        sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteAlreadyOffOther().replaceAll("%p%", playerData.getName())));
    }

    public void subCommandProteOff(CommandSender sender){
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        PlayerData playerData = playerDataManager.getPlayerData((Player) sender);
        if(playerData.isProtected()) {
            playerDataManager.toggleProtectionPlayer(playerData, false);
            plugin.getTaskManager().stopAllTask(playerData);
            sender.sendMessage(ColoredMessage.setColor(plugin.getMessagesManager().getProteOffYourself()));
            if(plugin.getConfigManager().isTitleProteOffEnabled()) {
                TitleUtils.sendTitle((Player) sender, CustomTitle.PROTE_OFF, plugin);
            }
            if(plugin.getConfigManager().isSoundProteOffEnabled()) {
                SoundUtils.sendSound((Player) sender, CustomSound.PROTE_OFF, plugin);
            }
            return;
        }
        sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getProteAlreadyOff()));
    }

    public void subCommandCreateZone(CommandSender sender, String[] args) {
        if (!sender.hasPermission("protectionpvp.admin.wand")) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoName()));
            return;
        }
        String zoneName = args[1];
        ZonesManager zonesManager = plugin.getZonesManager();
        if (zonesManager.zoneAlreadyExist(zoneName)) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNameExist()));
            return;
        }
        if (!zonesManager.twoCornersSelected((Player) sender)) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoSelected()));
            return;
        }
        Location corner1 = ProtectionPvP.cacheLocCorner1.get(sender);
        Location corner2 = ProtectionPvP.cacheLocCorner2.get(sender);
        if(plugin.getZonesManager().isZoneOverlap(corner1, corner2)){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getZoneOverlap()));
            return;
        }
        ZonePvP zonePvP = new ZonePvP(zoneName.toLowerCase(), corner1.getWorld().getName(),
                corner1, corner2);
        zonesManager.createNewZone(zonePvP);
        zonesManager.restorePlayerCornerCache((Player) sender);
        sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneCreate()));
    }

    public void subCommandDeleteZone(CommandSender sender, String[] args) {
        if (!sender.hasPermission("protectionpvp.admin.deletezone")) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoName()));
            return;
        }
        String zoneName = args[1];
        ZonesManager zonesManager = plugin.getZonesManager();
        if (!zonesManager.zoneAlreadyExist(zoneName)) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoExist()));
            return;
        }
        zonesManager.removeZone(zonesManager.getZoneByName(zoneName));
        sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneDelete()));
    }

    public void subCommandRenameZone(CommandSender sender, String[] args){
        // /protepvp renamezone (old) (new)
        if(!sender.hasPermission("protectionpvp.admin.renamezone")){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        if(args.length != 3){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getIncorrectCommand()));
            return;
        }
        String oldName = args[1].toLowerCase();
        String newName = args[2].toLowerCase();
        ZonesManager zonesManager = plugin.getZonesManager();
        if(zonesManager.getZoneByName(oldName) == null){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoExist()));
            return;
        }
        if(zonesManager.getZoneByName(newName) != null){
            sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getZoneNameExist()));
            return;
        }
        ZonePvP zone = zonesManager.getZoneByName(oldName);
        zonesManager.setName(zone, newName);
        sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getZoneRename()
                .replaceAll("%m1%", oldName).replaceAll("%m2%", newName)));
    }

    public void subCommandWand(CommandSender sender) {
        if (!sender.hasPermission("protectionpvp.admin.wand")) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        Player player = (Player) sender;
        player.getInventory().addItem(ItemStackUtils.createWandSelector(plugin));
        sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getWandGive()));
    }

    public void subCommandListZones(CommandSender sender) {
        if (!sender.hasPermission("protectionpvp.admin.listzones")) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        ZonesManager zonesManager = plugin.getZonesManager();
        StringBuilder listString = new StringBuilder();
        int i = 1;
        for (ZonePvP zone : zonesManager.getZones()) {
            if(zonesManager.getZones().size() != i) {
                listString.append(zone.getName()).append(", ");
            }else{
                listString.append(zone.getName());
            }
            i++;
        }
        if(!listString.isEmpty()) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneList()
                    .replaceAll("%m%", listString.toString())));
            return;
        }
        sender.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getZoneListEmpty()));
    }

    public void subCommandSetTpZone(CommandSender sender, String[] args){
        if (!sender.hasPermission("protectionpvp.admin.settpzone")) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        if (args.length == 1) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoName()));
            return;
        }
        String zoneName = args[1];
        ZonesManager zonesManager = plugin.getZonesManager();
        if (!zonesManager.zoneAlreadyExist(zoneName)) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getZoneNoExist()));
            return;
        }
        Player player = (Player) sender;
        Location tpZone = player.getLocation();
        plugin.getZonesManager().setTpZone(zonesManager.getZoneByName(zoneName), tpZone);
        player.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getZoneSetTp()));
    }

    public void subCommandReload(CommandSender sender) {
        if (!sender.hasPermission("protectionpvp.admin.reload")) {
            sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getNoPermission()));
            return;
        }
        plugin.reloadPlugin();
        sender.sendMessage(ProtectionPvP.prefix + ColoredMessage.setColor(plugin.getMessagesManager().getReload()));
    }

    public void help(CommandSender sender, int type){
        if(type == 1){
            List<String> help = plugin.getMessagesManager().getHelpPlayer();
            for(String m : help){
                sender.sendMessage(ColoredMessage.setColor(m));
            }
        }else{
            List<String> help = plugin.getMessagesManager().getHelpAdmin();
            for(String m : help){
                sender.sendMessage(ColoredMessage.setColor(m));
            }
        }
    }
}
