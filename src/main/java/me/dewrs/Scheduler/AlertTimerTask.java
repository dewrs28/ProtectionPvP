package me.dewrs.Scheduler;

import me.dewrs.Enums.CustomSound;
import me.dewrs.Enums.CustomTitle;
import me.dewrs.Model.PlayerData;
import me.dewrs.ProtectionPvP;
import me.dewrs.Utils.ColoredMessage;
import me.dewrs.Utils.SoundUtils;
import me.dewrs.Utils.TitleUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AlertTimerTask extends BukkitRunnable {
    private ProtectionPvP plugin;
    private PlayerData playerData;
    private int nextAlertTime;
    private List<Integer> alertTimes;

    public AlertTimerTask(ProtectionPvP plugin, PlayerData playerData, Integer nextAlertTime, List<Integer> alertTimes) {
        this.plugin = plugin;
        this.playerData = playerData;
        this.nextAlertTime = nextAlertTime;
        this.alertTimes = alertTimes;
    }

    @Override
    public void run() {
        Player player = plugin.getPlayerDataManager().getPlayerByPlayerData(playerData);
        String countdown = plugin.getPlayerDataManager().getCountdown(playerData);
        player.sendMessage(ColoredMessage.setColor(plugin.getMessagesManager().getProteAlert().replaceAll("%t%", countdown)));
        if(plugin.getConfigManager().isTitleProteAlertEnabled()){
            TitleUtils.sendTitle(player, CustomTitle.PROTE_ALERT, plugin);
        }
        if(plugin.getConfigManager().isSoundProteAlertEnabled()){
            SoundUtils.sendSound(player, CustomSound.PROTE_ALERT, plugin);
        }
        plugin.getTaskManager().scheduleNextAlert(playerData, nextAlertTime, alertTimes, false);
    }
}
