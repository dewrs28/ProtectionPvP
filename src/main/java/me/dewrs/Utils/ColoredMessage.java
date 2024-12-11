package me.dewrs.Utils;

import org.bukkit.ChatColor;

public class ColoredMessage {
    public static String setColor(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
