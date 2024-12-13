package me.dewrs.Managers;

import me.dewrs.Model.PlayerData;
import me.dewrs.ProtectionPvP;
import me.dewrs.Scheduler.AlertTimerTask;
import me.dewrs.Scheduler.TogglePvPTask;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private ProtectionPvP plugin;

    public TaskManager(ProtectionPvP plugin) {
        this.plugin = plugin;
    }

    public void startTaskProteOn(PlayerData playerData, int time, boolean isReJoin){
        TogglePvPTask togglePvPTask = new TogglePvPTask(plugin, playerData);
        togglePvPTask.runTaskLater(plugin,time*20L);
        playerData.setTaskID(togglePvPTask.getTaskId());
        startTaskAlert(playerData, isReJoin);
    }

    public void startTaskAlert(PlayerData playerData, boolean isReJoin) {
        int timeLeft = plugin.getPlayerDataManager().getNewTimeProtectedPlayer(playerData);
        List<Integer> alertTimes = new ArrayList<>(playerData.getListAlertTimes());
        scheduleNextAlert(playerData, timeLeft, alertTimes, isReJoin);
    }

    public void scheduleNextAlert(PlayerData playerData, int timeLeft, List<Integer> alertTimes, boolean isReJoin) {
        int nextAlertTime;
        if(!isReJoin) {
            if(alertTimes.isEmpty()){
                return;
            }
            nextAlertTime = alertTimes.remove(0);
        }else{
            if(!alertTimes.isEmpty()) {
                nextAlertTime = alertTimes.get(0) + plugin.getConfigManager().getTimeToAlertProte();
            }else{
                nextAlertTime = plugin.getConfigManager().getTimeToAlertProte();
            }
        }
        int delayInTicks = (timeLeft - nextAlertTime) * 20;
        if (delayInTicks > 0) {
            plugin.getPlayerDataManager().setListAlertTimes(playerData, new ArrayList<>(alertTimes));
            AlertTimerTask alertTimerTask = new AlertTimerTask(plugin, playerData, nextAlertTime, alertTimes);
            alertTimerTask.runTaskLater(plugin, (delayInTicks));
            playerData.setAlertTaskID(alertTimerTask.getTaskId());
        }
    }

    public void stopAllTask(PlayerData playerData){
        if(playerData.getTaskID() != 0) Bukkit.getScheduler().cancelTask(playerData.getTaskID());
        playerData.setTaskID(0);
        if(playerData.getAlertTaskID() != 0) Bukkit.getScheduler().cancelTask(playerData.getAlertTaskID());
        playerData.setAlertTaskID(0);
    }
}
