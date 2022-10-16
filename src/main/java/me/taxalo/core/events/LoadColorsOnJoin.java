package me.taxalo.core.events;

import com.nametagedit.plugin.NametagEdit;
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

        if (colorManager.isLoaded(player.getUniqueId())) {

            NametagEdit.getApi().setPrefix(player, colorManager.getColor(player.getUniqueId()));

            return;
        }


        MongoDB mongoHandler = plugin.getMongoHandler();
        final Document user = mongoHandler.getUser(player.getUniqueId());

        if (user == null) return;

        colorManager.setColor(player.getUniqueId(), user.get("color").toString());
    }
}
