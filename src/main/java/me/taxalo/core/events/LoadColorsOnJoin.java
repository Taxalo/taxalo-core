package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.managers.ColorManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadColorsOnJoin implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ColorManager colorManager = plugin.getColorManager();
        if (colorManager == null) {
            event.getPlayer().kickPlayer("Your data was not loaded yet. Try again.");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (colorManager.isLoaded(event.getPlayer().getUniqueId())) return;

            MongoDB mongoHandler = plugin.getMongoHandler();
            Document user = mongoHandler.getUser(event.getPlayer().getUniqueId());
            if (user == null) return;
            colorManager.setColor(event.getPlayer().getUniqueId(), user.get("color").toString());
        });

    }
}
