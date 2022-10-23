package me.taxalo.core.managers;

import lombok.val;
import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.utils.Rank;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class RankManager {
    final Core plugin;
    final MongoDB mongoHandler;
    final PermissionManager permissionManager;
    private final HashMap<UUID, List<String>> userList;
    private final HashMap<String, Rank> ranksList;

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

                final List<String> ranks = user.getList("ranks", String.class);

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
                final String rankName = rank.get("name").toString();
                final List<String> permissions = rank.getList("permissions", String.class);
                String prefix = rank.getString("prefix");
                int priority = rank.getInteger("priority", 1);

                if (prefix == null) prefix = "";

                val newRank = new Rank(permissions, prefix, priority);

                ranksList.put(rankName, newRank);
            }
        });
    }

    public List<String> getPermissions(String rankName) {
        return ranksList.get(rankName).getPermissions();
    }

    public List<String> getUser(UUID uuid) {
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

        val emptyRank = new Rank(Collections.emptyList(), "", 1);

        ranksList.put(rankName, emptyRank);
        mongoHandler.addRank(rankName, null);
    }

    public void removeRank(String rankName) {
        if (!ranksList.containsKey(rankName)) return;
        ranksList.remove(rankName);
        mongoHandler.removeRank(rankName);
    }

    public void setUser(UUID uuid, List<String> ranks) {
        userList.put(uuid, ranks);
        mongoHandler.setUserRanks(uuid, ranks);

        final Player player = Bukkit.getServer().getPlayer(uuid);
        if (player == null) return;

        permissionManager.removeAttachment(player);
        permissionManager.addAttachment(player);

        val scoreboard = plugin.getScoreboard();
        if (scoreboard == null) return;

        val team = scoreboard.getTeam(player.getName());
        if (team == null) return;

        setColoredPrefix(team, getHighestPrefix(player));
    }

    public void setPrefix(String rankName, String prefix) {
        if (!ranksList.containsKey(rankName)) return;

        val rank = ranksList.get(rankName);

        rank.setPrefix(prefix);
        mongoHandler.setRankPrefix(rankName, prefix);

        val scoreboard = plugin.getScoreboard();

        // Apply prefix to every player whose highest Rank is rankName

        for (UUID uuid : getUsersWithRank(rankName)) {
            final Player player = Bukkit.getServer().getPlayer(uuid);
            if (player == null) continue;

            if (!Objects.equals(getHighestPrefix(player), prefix)) continue;

            val team = scoreboard.getTeam(player.getName());
            if (team == null) continue;

            setColoredPrefix(team, prefix);
        }

    }

    public void setPriority(String rankName, int priority) {
        if (!ranksList.containsKey(rankName)) return;

        val rank = ranksList.get(rankName);

        rank.setPriority(priority);
        mongoHandler.setRankPriorty(rankName, priority);

        final Scoreboard scoreboard = plugin.getScoreboard();

        for (UUID uuid : getUsersWithRank(rankName)) {
            final Player player = Bukkit.getServer().getPlayer(uuid);
            if (player == null) continue;

            val team = scoreboard.getTeam(player.getName());
            if (team == null) continue;

            setColoredPrefix(team, getHighestPrefix(player));
        }
    }

    public String getHighestPrefix(Player player) {
        val user = userList.get(player.getUniqueId());

        if (user == null) return null;

        val highestRank = user.stream()
                .map(ranksList::get)
                .max(Comparator.comparingInt(Rank::getPriority))
                .orElse(null);

        if (highestRank == null) return "";

        return highestRank.getPrefix();
    }

    public void addPermission(String rankName, String permission) {
        val rank = ranksList.get(rankName);

        val permissions = rank.getPermissions();

        if (permissions.contains(permission)) return;

        permissions.add(permission);

        ranksList.put(rankName, rank);

        mongoHandler.addPermission(rankName, permission);

        val users = getUsersWithRank(rankName);

        for (UUID uuid : users) {
            permissionManager.addPermission(uuid, permission);
        }
    }

    public void removePermission(String rankName, String permission) {
        val rank = ranksList.get(rankName);

        val permissions = rank.getPermissions();

        if (!permissions.contains(permission)) return;

        permissions.remove(permission);
        ranksList.put(rankName, rank);

        val users = getUsersWithRank(rankName);

        mongoHandler.removePermission(rankName, permission);

        for (UUID uuid : users) {
            permissionManager.removePermission(uuid, permission);
        }
    }

    public void setColoredPrefix(Team team, String prefix) {
        final String space = prefix.length() == 0 ? "" : " ";
        team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix + space));
    }

}
