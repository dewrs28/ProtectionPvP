package me.dewrs.Scheduler;

import me.dewrs.Managers.PlayerDataManager;
import me.dewrs.Model.PlayerData;
import me.dewrs.ProtectionPvP;
import me.dewrs.Utils.ColoredMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TogglePvPTask extends BukkitRunnable {
    private ProtectionPvP plugin;
    private PlayerData playerData;

    public TogglePvPTask(ProtectionPvP plugin, PlayerData playerData) {
        this.plugin = plugin;
        this.playerData = playerData;
    }

    @Override
    public void run() {
        PlayerDataManager playerDataManager = plugin.getPlayerDataManager();
        Player player = Bukkit.getPlayer(UUID.fromString(playerData.getUuid()));
        if(player != null) player.sendMessage(ColoredMessage.setColor(plugin.getMessagesManager().getProteOffYourself()));
        playerDataManager.toggleProtectionPlayer(playerData, false);
    }
}
