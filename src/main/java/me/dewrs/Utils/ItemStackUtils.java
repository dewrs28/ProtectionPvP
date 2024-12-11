package me.dewrs.Utils;

import me.dewrs.ProtectionPvP;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackUtils {
    public static ItemStack createWandSelector(ProtectionPvP plugin){
        String name = plugin.getConfigManager().getNameWand();
        List<String> lore = plugin.getConfigManager().getLoreWand();
        List<String> loreColored = new ArrayList<>();
        ItemStack item = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColoredMessage.setColor(name));
        for(String m : lore){
            loreColored.add(ColoredMessage.setColor(m));
        }
        meta.setLore(loreColored);
        item.setItemMeta(meta);
        return item;
    }
}
