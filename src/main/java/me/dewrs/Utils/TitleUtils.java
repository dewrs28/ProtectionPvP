package me.dewrs.Utils;

import me.dewrs.Enums.CustomTitle;
import me.dewrs.Model.PlayerData;
import me.dewrs.ProtectionPvP;
import org.bukkit.entity.Player;

public class TitleUtils {

    public static void sendTitle(Player player, CustomTitle customTitle, ProtectionPvP plugin){
        String up = "";
        String down = "";
        if(customTitle == CustomTitle.PROTE_ON){
            up = ColoredMessage.setColor(plugin.getConfigManager().getTitleProteOnUp());
            down = ColoredMessage.setColor(plugin.getConfigManager().getTitleProteOnDown());
        }
        if(customTitle == CustomTitle.PROTE_OFF){
            up = ColoredMessage.setColor(plugin.getConfigManager().getTitleProteOffUp());
            down = ColoredMessage.setColor(plugin.getConfigManager().getTitleProteOffDown());
        }
        if(customTitle == CustomTitle.PROTE_ALERT){
            PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
            up = ColoredMessage.setColor(plugin.getConfigManager().getTitleProteAlertUp()
                    .replaceAll("%t%", plugin.getPlayerDataManager().getCountdown(playerData)));
            down = ColoredMessage.setColor(plugin.getConfigManager().getTitleProteAlertDown()
                    .replaceAll("%t%", plugin.getPlayerDataManager().getCountdown(playerData)));
        }
        player.sendTitle(up, down, 10, 35, 10);
    }
}
