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
        final ColorManager colorManager = Core.getInstance().getColorManager();

        final String newFormat = colorManager.getColor(e.getPlayer().getUniqueId()) + "%1$s" + ChatColor.DARK_GRAY + ":" + ChatColor.WHITE + " %2$s";

        e.setFormat(newFormat);
    }
}
