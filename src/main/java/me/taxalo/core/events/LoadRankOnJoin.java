package me.taxalo.core.events;

import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.managers.PermissionManager;
import me.taxalo.core.managers.RankManager;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoadRankOnJoin implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final RankManager rankManager = plugin.getRankManager();
        if (rankManager == null) {
            event.getPlayer().kickPlayer("Your data was not loaded yet. Try again.");
            return;
        }
        final PermissionManager permissionManager = plugin.getPermissionManager();
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            if (rankManager.getUser(playerUUID) != null) {
                if (!permissionManager.isLoaded(playerUUID)) permissionManager.addAttachment(player);
                return;
            }

            final MongoDB mongoHandler = plugin.getMongoHandler();
            final Document user = mongoHandler.getUser(playerUUID);

            rankManager.setUser(playerUUID, user != null ? user.getList("ranks", String.class) : new ArrayList<>());
            permissionManager.addAttachment(player);
        });

    }
}
