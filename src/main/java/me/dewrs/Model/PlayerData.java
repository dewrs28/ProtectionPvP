package me.dewrs.Model;

import java.util.ArrayList;

public class PlayerData {
    private final String uuid;
    private String name;
    private long cooldownMillis;
    private int actualProtectionTime;
    private boolean isProtected;
    private int taskID;
    private int alertTaskID;
    private ArrayList<Integer> listAlertTimes;

    public PlayerData(String uuid) {
        this.uuid = uuid;
        taskID = 0;
        alertTaskID = 0;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    public void setCooldownMillis(long cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }

    public int getActualProtectionTime() {
        return actualProtectionTime;
    }

    public void setActualProtectionTime(int actualProtectionTime) {
        this.actualProtectionTime = actualProtectionTime;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getAlertTaskID() {
        return alertTaskID;
    }

    public void setAlertTaskID(int alertTaskID) {
        this.alertTaskID = alertTaskID;
    }

    public ArrayList<Integer> getListAlertTimes() {
        return listAlertTimes;
    }

    public void setListAlertTimes(ArrayList<Integer> listAlertTimes) {
        this.listAlertTimes = listAlertTimes;
    }
}
