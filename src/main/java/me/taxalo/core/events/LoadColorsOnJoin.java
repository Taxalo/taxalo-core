package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.managers.ColorManager;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadColorsOnJoin implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final ColorManager colorManager = plugin.getColorManager();
        final Player player = event.getPlayer();

        if (colorManager == null) {
            player.kickPlayer("Your data was not loaded yet. Try again.");
            return;
        }

        if (colorManager.isLoaded(player.getUniqueId())) {
            colorManager.addToTeam(player.getName(), colorManager.getColor(player.getUniqueId()));
            player.setScoreboard(plugin.getScoreboard());
            return;
        }

        final MongoDB mongoHandler = plugin.getMongoHandler();
        final Document user = mongoHandler.getUser(player.getUniqueId());

        String color = user == null ? ChatColor.GRAY.toString() : user.getString("color");

        colorManager.setColor(player.getUniqueId(), color);
        player.setScoreboard(plugin.getScoreboard());
    }
}
