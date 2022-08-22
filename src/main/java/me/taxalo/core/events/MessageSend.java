package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.managers.ColorManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageSend implements Listener {

    @EventHandler
    public void PlayerChatEvent(AsyncPlayerChatEvent e) {
        ColorManager colorManager = Core.getInstance().getColorManager();
        String newFormat = colorManager.getColor(e.getPlayer().getUniqueId()) + "<player>" + ChatColor.DARK_GRAY + ":" + ChatColor.WHITE + " <message>";
        newFormat = newFormat
                .replace("<player>", "%1$s")
                .replace("<message>", "%2$s");
        e.setFormat(newFormat);
    }
}
