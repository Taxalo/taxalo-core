package me.taxalo.core.managers;

import lombok.val;
import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class RankManager {
    final Core plugin;
    final MongoDB mongoHandler;
    final PermissionManager permissionManager;
    private final HashMap<UUID, ArrayList<String>> userList;
    private final HashMap<String, ArrayList<String>> ranksList;

    public RankManager(Core core) {
        plugin = core;
        mongoHandler = core.getMongoHandler();
        permissionManager = core.getPermissionManager();
        userList = new HashMap<>();
        ranksList = new HashMap<>();
        loadRanks();
        loadUsers();
    }

    public void loadUsers() {
        final MongoDB mongoHandler = plugin.getMongoHandler();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                final Document user = mongoHandler.getUser(player.getUniqueId());

                ArrayList<String> ranks = (ArrayList<String>) user.get("ranks");

                if (ranks == null) continue;
                userList.put(player.getUniqueId(), ranks);
            }
        });
    }

    public void loadRanks() {
        final MongoDB mongoHandler = plugin.getMongoHandler();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            val ranks = mongoHandler.getRanks();
            if (ranks == null) return;
            for (Document rank : ranks) {
                ranksList.put(rank.get("name").toString(), (ArrayList<String>) rank.get("permissions"));
            }
        });
    }

    public ArrayList<String> getPermissions(String rankName) {
        return ranksList.get(rankName);
    }

    public ArrayList<String> getUser(UUID uuid) {
        return userList.get(uuid);
    }

    public ArrayList<UUID> getUsersWithRank(String rankName) {
        return new ArrayList<>(userList.entrySet()
                .stream()
                .filter(user -> user.getValue()
                        .stream()
                        .anyMatch(rank -> Objects.equals(rank, rankName)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .keySet());
    }

    public boolean rankExists(String rankName) {
        val rank = ranksList.get(rankName);
        return rank != null;
    }

    public ArrayList<String> getRanks() {
        return new ArrayList<>(ranksList.keySet());
    }

    public void addRank(String rankName) {
        if (ranksList.containsKey(rankName)) return;
        ranksList.put(rankName, new ArrayList<>());
        mongoHandler.addRank(rankName, null);
    }

    public void removeRank(String rankName) {
        if (!ranksList.containsKey(rankName)) return;
        ranksList.remove(rankName);
        mongoHandler.removeRank(rankName);
    }

    public void setUser(UUID uuid, ArrayList<String> ranks) {
        userList.put(uuid, ranks);
        mongoHandler.setUserRanks(uuid, ranks);

        final Player player = Bukkit.getServer().getPlayer(uuid);
        if (player == null) return;

        permissionManager.removeAttachment(player);
        permissionManager.addAttachment(player);
    }

    public void addPermission(String rankName, String permission) {
        val rank = ranksList.get(rankName);

        if (rank.contains(permission)) return;

        rank.add(permission);
        ranksList.put(rankName, rank);

        mongoHandler.addPermission(rankName, permission);

        val users = getUsersWithRank(rankName);

        for (UUID uuid : users) {
           permissionManager.addPermission(uuid, permission);
        }
    }

    public void removePermission(String rankName, String permission) {
        val rank = ranksList.get(rankName);

        if (!rank.contains(permission)) return;

        rank.remove(permission);
        ranksList.put(rankName, rank);

        val users = getUsersWithRank(rankName);

        mongoHandler.removePermission(rankName, permission);

        for (UUID uuid : users) {
            permissionManager.removePermission(uuid, permission);
        }
    }

}
