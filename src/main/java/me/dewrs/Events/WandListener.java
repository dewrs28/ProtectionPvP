package me.dewrs.Events;

import me.dewrs.Managers.ZonesManager;
import me.dewrs.ProtectionPvP;
import me.dewrs.Utils.ColoredMessage;
import me.dewrs.Utils.ItemStackUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandListener implements Listener {
    private ProtectionPvP plugin;

    public WandListener(ProtectionPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSelect(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(!player.hasPermission("protectionpvp.admin.wand")){
            return;
        }

        if(event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        ItemStack item = event.getItem();
        if(item == null || event.getClickedBlock() == null){
            return;
        }

        ItemStack wandSelector = ItemStackUtils.createWandSelector(plugin);
        if(!item.isSimilar(wandSelector)){
            return;
        }
        event.setCancelled(true);
        Action action = event.getAction();
        ZonesManager zonesManager = plugin.getZonesManager();
        Location location = event.getClickedBlock().getLocation();
        if(action == Action.LEFT_CLICK_BLOCK){
            if(ProtectionPvP.cacheLocCorner1.get(player) != null && ProtectionPvP.cacheLocCorner1.get(player).equals(location)){
                return;
            }
            ProtectionPvP.cacheLocCorner1.put(player, location);
            if(!zonesManager.twoCornersSameWorld(player)){
                zonesManager.restorePlayerCornerCache(player);
            }
            ProtectionPvP.cacheLocCorner1.put(player, location);
            player.sendMessage(ProtectionPvP.prefix+ ColoredMessage.setColor(plugin.getMessagesManager().getWandSel()
                    .replaceAll("%i%", "1")));
        }else{
            if(ProtectionPvP.cacheLocCorner2.get(player) != null && ProtectionPvP.cacheLocCorner2.get(player).equals(location)){
                return;
            }
            ProtectionPvP.cacheLocCorner2.put(player, location);
            if(!zonesManager.twoCornersSameWorld(player)){
                zonesManager.restorePlayerCornerCache(player);
            }
            ProtectionPvP.cacheLocCorner2.put(player, location);
            player.sendMessage(ProtectionPvP.prefix+ ColoredMessage.setColor(plugin.getMessagesManager().getWandSel()
                    .replaceAll("%i%", "2")));
        }
    }
}
