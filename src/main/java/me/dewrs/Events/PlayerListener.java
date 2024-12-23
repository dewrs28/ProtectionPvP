package me.dewrs.Events;

import me.dewrs.Api.ToggleProtectionEvent;
import me.dewrs.Managers.PlayerDataManager;
import me.dewrs.Managers.ZonesManager;
import me.dewrs.Model.PlayerData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.Packets.ProtocolLibHook;
import me.dewrs.ProtectionPvP;
import me.dewrs.Utils.ColoredMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {
    private ProtectionPvP plugin;

    public PlayerListener(ProtectionPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        PlayerData playerData = playerDataManager.getPlayerData(player);
        if(playerData != null && playerData.isProtected()) {
            playerDataManager.reStartProtectionPlayer(playerData);
            if (plugin.getConfigManager().isGlassWallsEnabled()) {
                ArrayList<ZonePvP> nearbyZones = plugin.getZonesManager().getNearbyZones(player.getLocation());
                plugin.getZoneViewerManager().updatePlayerZones(player, nearbyZones);
            }
            if(!plugin.getConfigManager().isProtePlayersCanEnterPvPZones()){
                plugin.getZonesManager().kickPlayerOfActualZone(player);
            }
        }
        playerDataManager.manageFirstTimeJoin(player);
        String latestVersion = plugin.getUpdateChecker().getLatestVersion();
        if((player.hasPermission("protectionpvp.admin") || player.isOp()) && !plugin.version.equals(latestVersion)){
            player.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor("&cThere is a new version of the plugin &8(&e"+latestVersion+"&8)"));
            player.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor("&cYou can download it at: &ehttps://spigotmc.org/resources/121277"));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        PlayerData playerData = playerDataManager.getPlayerData(player);
        if(playerData.isProtected()){
            plugin.getTaskManager().stopAllTask(playerData);
            playerDataManager.setProtectionTime(playerData, playerDataManager.getNewTimeProtectedPlayer(playerData));
        }
        plugin.getZoneViewerManager().removeFullPlayer(player);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        if(!event.getEntity().getType().equals(EntityType.PLAYER) || !event.getDamager().getType().equals(EntityType.PLAYER)){
            return;
        }
        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        PlayerData playerDataVictim = playerDataManager.getPlayerData(victim);
        PlayerData playerDataDamager = playerDataManager.getPlayerData(damager);
        if(playerDataVictim.isProtected() && !playerDataDamager.isProtected()) {
            damager.sendMessage(ProtectionPvP.prefix+ ColoredMessage.setColor(plugin.getMessagesManager().getVictimProteOn()));
            event.setCancelled(true);
        }else if(playerDataDamager.isProtected() && !playerDataVictim.isProtected()){
            damager.sendMessage(ProtectionPvP.prefix+ ColoredMessage.setColor(plugin.getMessagesManager().getDamagerProteOn()));
            event.setCancelled(true);
        }else if(playerDataVictim.isProtected() && playerDataDamager.isProtected()){
            damager.sendMessage(ProtectionPvP.prefix+ ColoredMessage.setColor(plugin.getMessagesManager().getDamagerProteOn()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() &&
                from.getBlockY() == to.getBlockY() &&
                from.getBlockZ() == to.getBlockZ()) {
            return;
        }
        if(plugin.getConfigManager().isProtePlayersCanEnterPvPZones()){
            return;
        }
        Location playerLocation = event.getPlayer().getLocation();
        ZonesManager zonesManager = plugin.getZonesManager();

        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData.isProtected()) {
            ArrayList<ZonePvP> nearbyZones = zonesManager.getNearbyZones(playerLocation);
            if (plugin.getConfigManager().isGlassWallsEnabled()) {
                plugin.getZoneViewerManager().updatePlayerZones(player, nearbyZones);
            }
            for (ZonePvP zone : nearbyZones) {
                if (zone.isInWithoutY(to)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        Location to = event.getTo();
        if(!playerData.isProtected()){
            return;
        }
        if(plugin.getConfigManager().isProtePlayersCanEnterPvPZones()){
            return;
        }
        if(to == null){
            return;
        }
        for(ZonePvP zone : plugin.getZonesManager().getZones()){
            if(zone.isInWithoutY(to)){
                event.setCancelled(true);
                player.sendMessage(ProtectionPvP.prefix+ColoredMessage.setColor(plugin.getMessagesManager().getTpZoneProteOn()));
            }
        }
    }
}
