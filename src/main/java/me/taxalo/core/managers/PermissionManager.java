package me.taxalo.core.managers;

import me.taxalo.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PermissionManager {

    Core plugin;
    HashMap<UUID, PermissionAttachment> attachments;

    public PermissionManager(Core core) {
        plugin = core;
        attachments = new HashMap<>();
    }

    public boolean isLoaded(UUID uuid) {
        return attachments.containsKey(uuid);
    }

    public void addPermission(UUID uuid, String permission) {
        PermissionAttachment permissionAttachment = attachments.get(uuid);

        Player player = Bukkit.getServer().getPlayer(uuid);

        if (player == null) return;
        if (permissionAttachment == null) {
            addAttachment(player);
            return;
        }
        if (permissionAttachment.getPermissions().containsKey(permission)) return;
        permissionAttachment.setPermission(permission, true);
    }

    public void removePermission(UUID uuid, String permission) {
        PermissionAttachment permissionAttachment = attachments.get(uuid);

        if (permissionAttachment == null) {
            Player player = Bukkit.getServer().getPlayer(uuid);

            if (player == null) return;

            addAttachment(player);
            return;
        }

        if (!permissionAttachment.getPermissions().containsKey(permission)) return;
        permissionAttachment.unsetPermission(permission);
    }

    public void addAttachment(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (attachments.get(playerUUID) != null) return;

        PermissionAttachment permissionAttachment = player.addAttachment(plugin);

        RankManager rankManager = plugin.getRankManager();

        List<String> ranks = rankManager.getUser(playerUUID);

        if (ranks != null && ranks.size() > 0) {
            for (String rank : ranks) {
                List<String> rankPermissions = rankManager.getPermissions(rank);
                if (rankPermissions == null) continue;
                for (String permission : rankPermissions) {
                    if (permissionAttachment.getPermissions().containsKey(permission)) continue;
                    permissionAttachment.setPermission(permission, true);
                }
            }
        }

        attachments.put(playerUUID, permissionAttachment);
    }

    public void removeAttachment(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (attachments.get(playerUUID) == null) return;

        player.removeAttachment(attachments.get(playerUUID));
        attachments.remove(playerUUID);
    }
}
