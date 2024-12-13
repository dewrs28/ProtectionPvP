package me.dewrs.Utils;

import me.dewrs.Enums.CustomSound;
import me.dewrs.ProtectionPvP;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {
    public static void sendSound(Player player, CustomSound customSound, ProtectionPvP plugin){
        Sound sound = Sound.BLOCK_NOTE_BLOCK_PLING;
        if (customSound == CustomSound.PROTE_ON) {
            sound = Sound.valueOf(plugin.getConfigManager().getSoundProteOn());
        }
        if(customSound == CustomSound.PROTE_OFF){
            sound = Sound.valueOf(plugin.getConfigManager().getSoundProteOff());
        }
        if(customSound == CustomSound.PROTE_ALERT){
            sound = Sound.valueOf(plugin.getConfigManager().getSoundProteAlert());
        }
        player.playSound(player.getLocation(), sound, 10, 2);
    }
}
