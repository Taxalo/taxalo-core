package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.managers.ColorManager;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.MM;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageSend implements Listener {

    @EventHandler
    public void PlayerChatEvent(AsyncPlayerChatEvent e) {
        final ColorManager colorManager = Core.getInstance().getColorManager();
        final RankManager rankManager = Core.getInstance().getRankManager();
        if (colorManager == null || rankManager == null) return;

        final String prefix = MM.translate(rankManager.getHighestPrefix(e.getPlayer()));
        final String color = colorManager.getColor(e.getPlayer().getUniqueId());
        final String formatText = ChatColor.DARK_GRAY + ":" + ChatColor.WHITE;

        final String newFormat = prefix + " " + color + "%1$s" + formatText  + " %2$s";

        e.setFormat(newFormat);
    }
}
