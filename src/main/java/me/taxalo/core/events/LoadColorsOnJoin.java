package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.managers.ColorManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadColorsOnJoin implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ColorManager colorManager = plugin.getColorManager();
        final Player player = event.getPlayer();

        if (colorManager == null) {
            player.kickPlayer("Your data was not loaded yet. Try again.");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (colorManager.isLoaded(player.getUniqueId())) {

                player.setPlayerListName(colorManager.getColor(player.getUniqueId())+ player.getName());

                return;
            }

            // Load and setplayer color

            MongoDB mongoHandler = plugin.getMongoHandler();
            Document user = mongoHandler.getUser(player.getUniqueId());

            if (user == null) return;

            colorManager.setColor(player.getUniqueId(), user.get("color").toString());

            player.setPlayerListName(colorManager.getColor(player.getUniqueId())+ player.getName());
        });

    }
}
