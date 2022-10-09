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
import java.util.UUID;

public class LoadRankOnJoin implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        RankManager rankManager = plugin.getRankManager();
        if (rankManager == null) {
            event.getPlayer().kickPlayer("Your data was not loaded yet. Try again.");
            return;
        }
        PermissionManager permissionManager = plugin.getPermissionManager();
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            if (rankManager.getUser(playerUUID) != null) {
                if (!permissionManager.isLoaded(playerUUID)) permissionManager.addAttachment(player);

                return;
            }

            MongoDB mongoHandler = plugin.getMongoHandler();
            Document user = mongoHandler.getUser(playerUUID);
            rankManager.setUser(playerUUID, user != null ? (ArrayList<String>) user.get("ranks") : new ArrayList<>());
            permissionManager.addAttachment(player);
        });

    }
}
