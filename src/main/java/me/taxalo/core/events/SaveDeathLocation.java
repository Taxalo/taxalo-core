package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.managers.BackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SaveDeathLocation implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        BackManager backManager = Core.getInstance().getBackManager();
        if (backManager == null) return;

        Player player = e.getEntity();
        backManager.setLocation(player.getUniqueId(), player.getLocation());
    }
}
