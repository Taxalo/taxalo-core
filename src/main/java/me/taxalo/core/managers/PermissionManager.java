package me.taxalo.core.managers;

import me.taxalo.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PermissionManager {

    final Core plugin;
    final HashMap<UUID, PermissionAttachment> attachments;

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
        final PermissionAttachment permissionAttachment = attachments.get(uuid);

        if (permissionAttachment == null) {
            final Player player = Bukkit.getServer().getPlayer(uuid);

            if (player == null) return;

            addAttachment(player);
            return;
        }

        if (!permissionAttachment.getPermissions().containsKey(permission)) return;
        permissionAttachment.unsetPermission(permission);
    }

    public void addAttachment(Player player) {
        final UUID playerUUID = player.getUniqueId();

        if (attachments.get(playerUUID) != null) return;

        final PermissionAttachment permissionAttachment = player.addAttachment(plugin);

        final RankManager rankManager = plugin.getRankManager();

        final ArrayList<String> ranks = rankManager.getUser(playerUUID);

        if (ranks != null && ranks.size() > 0) {
            for (String rank : ranks) {
                ArrayList<String> rankPermissions = rankManager.getPermissions(rank);
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
        final UUID playerUUID = player.getUniqueId();

        if (attachments.get(playerUUID) == null) return;

        player.removeAttachment(attachments.get(playerUUID));
        attachments.remove(playerUUID);
    }
}
