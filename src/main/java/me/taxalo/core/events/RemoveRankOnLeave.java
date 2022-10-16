package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.managers.PermissionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RemoveRankOnLeave implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        final PermissionManager permissionManager = plugin.getPermissionManager();

        if (permissionManager == null) return;

        if (permissionManager.isLoaded(e.getPlayer().getUniqueId()))
            permissionManager.removeAttachment(e.getPlayer());
    }
}
