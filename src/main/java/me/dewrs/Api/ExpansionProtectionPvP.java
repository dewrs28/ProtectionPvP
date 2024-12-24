package me.dewrs.Api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dewrs.ProtectionPvP;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExpansionProtectionPvP extends PlaceholderExpansion {
    private ProtectionPvP plugin;
    public ExpansionProtectionPvP(ProtectionPvP plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "protectionpvp";
    }

    @Override
    public @NotNull String getAuthor() {
        return "dewrs";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){
        if(player == null){
            return "";
        }
        if(identifier.equals("time")){
            return ProtectionPvPAPI.getActualProtectionTime(player)+"";
        }
        if(identifier.equals("status")){
            return ProtectionPvPAPI.isProtected(player)+"";
        }
        return null;
    }
}
