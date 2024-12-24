package me.dewrs.Api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ToggleProtectionEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final boolean status;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ToggleProtectionEvent(Player player, boolean status){
        this.player = player;
        this.status = status;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isProtected() {
        return status;
    }
}
