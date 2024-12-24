package me.dewrs.Packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.MovingObjectPositionBlock;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.dewrs.Model.ZonePvP;
import me.dewrs.ProtectionPvP;
import me.dewrs.Model.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class ProtocolLibHook {
    private ProtectionPvP main;
    private ProtocolManager manager;

    public ProtocolLibHook(ProtectionPvP main) {
        this.main = main;
        manager = ProtocolLibrary.getProtocolManager();
    }

    public void sendBlockUpdate(Player player, Location location){
        try{
            PacketContainer packet = manager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            // Configurar el paquete
            packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            packet.getBlockData().write(0, WrappedBlockData.createData(Material.RED_STAINED_GLASS));

            // Enviar el paquete al jugador
            manager.sendServerPacket(player, packet);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void removeBlockUpdate(Player player, Location location){
        try{
            PacketContainer packet = manager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
            // Configurar el paquete
            packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            packet.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));

            // Enviar el paquete al jugador
            manager.sendServerPacket(player, packet);
        }catch (Exception ex){
            ex.printStackTrace();
            player.sendMessage(ex.getMessage());
        }
    }

    public void addBlockDigInterception() {
        manager.addPacketListener(new PacketAdapter(main, ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() ==
                        PacketType.Play.Client.BLOCK_DIG) {

                    PacketContainer packet = event.getPacket();
                    Player player = event.getPlayer();

                    EnumWrappers.PlayerDigType digType = packet.getPlayerDigTypes().read(0);

                    if (digType != EnumWrappers.PlayerDigType.ABORT_DESTROY_BLOCK &&
                            digType != EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                        return;
                    }

                    BlockPosition blockPos = packet.getBlockPositionModifier().read(0);
                    Location blockLocation = new Location(player.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ());

                    for(ZonePvP zone : main.getZonesManager().getZones()){
                        if (zone.getWalls().contains(blockLocation) && digType == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
                            refreshFakeBlock(player, blockLocation);
                        }
                    }
                }
            }
        });
    }

    public void addBlockInteractInterceptionPaper() {
        manager.addPacketListener(new PacketAdapter(main, ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_PLACE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                EnumWrappers.Hand hand = event.getPacket().getHands().read(0);
                if(hand == EnumWrappers.Hand.OFF_HAND){
                    return;
                }
                MovingObjectPositionBlock objectPosition = event.getPacket().getMovingBlockPositions().read(0);
                BlockPosition blockPosition = objectPosition.getBlockPosition();
                Location blockLocation = new Location(player.getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                if(!main.getZoneViewerManager().isViewingBlock(player, blockLocation)){
                    return;
                }
                for(ZonePvP zone : main.getZonesManager().getZones()){
                    if(zone.getWalls().contains(blockLocation)){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sendBlockUpdate(player,blockLocation);
                            }
                        }.runTaskLater(plugin, 1);
                    }
                }
            }
        });
    }

    public void addBlockInteractInterceptionSpigot() {
        manager.addPacketListener(new PacketAdapter(main, ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ITEM) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                EnumWrappers.Hand hand = event.getPacket().getHands().read(0);
                if(hand == EnumWrappers.Hand.OFF_HAND){
                    return;
                }

                MovingObjectPositionBlock objectPosition = event.getPacket().getMovingBlockPositions().read(0);
                BlockPosition blockPosition = objectPosition.getBlockPosition();
                Location blockLocation = new Location(player.getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                if(!main.getZoneViewerManager().isViewingBlock(player, blockLocation)){
                    return;
                }
                for(ZonePvP zone : main.getZonesManager().getZones()){
                    if(zone.getWalls().contains(blockLocation)){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                sendBlockUpdate(player,blockLocation);
                            }
                        }.runTaskLater(plugin, 1);
                    }
                }
            }
        });
    }

    private void refreshFakeBlock(Player player, Location location) {
        PacketContainer fakeBlockPacket = manager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);

        fakeBlockPacket.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        fakeBlockPacket.getBlockData().write(0, WrappedBlockData.createData(Material.AIR));

        sendBlockUpdate(player, location);
    }
}
