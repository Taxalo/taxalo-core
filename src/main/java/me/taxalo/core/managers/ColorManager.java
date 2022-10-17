package me.taxalo.core.managers;

import me.taxalo.core.Core;
import me.taxalo.core.database.MongoDB;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ColorManager {

    final Core plugin;
    private final HashMap<UUID, String> userColors;

    public ColorManager(Core core) {
        userColors = new HashMap<>();
        plugin = core;
    }

    public Boolean isLoaded(UUID uuid) {
        String color = userColors.get(uuid);
        return color != null;
    }

    public String getColor(UUID uuid) {
        String color = userColors.get(uuid);
        return color == null ? ChatColor.GRAY.toString() : color;
    }

    public void loadColors() {
        MongoDB mongoHandler = plugin.getMongoHandler();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                Document user = mongoHandler.getUser(player.getUniqueId());
                String color = user.get("color").toString();
                userColors.put(player.getUniqueId(), color);
            }
        });
    }

    public void setColor(UUID uuid, String color) {
        userColors.put(uuid, color);
        final MongoDB mongoHandler = plugin.getMongoHandler();

        mongoHandler.setColor(uuid, color);

        // Update tabname
        Player player = Bukkit.getServer().getPlayer(uuid);

        if (player == null) return;

        addToTeam(player.getName(), color);
    }

    public void addToTeam(String playerName, String color) {
        Team playerTeam = createTeam(playerName, color);

        if (playerTeam == null) {
            return;
        }

        playerTeam.addEntry(playerName);
    }

    public Team createTeam(String playerName, String color) {
        Scoreboard teamScoreboard = plugin.getScoreboard();

        Team team = teamScoreboard.getTeam(playerName);

        ChatColor chatColor = Objects.requireNonNull(ChatColor.getByChar(color.substring(1)));

        if (team != null) {
            team.setColor(chatColor);
            return null;
        }

        Team teamName = teamScoreboard.registerNewTeam(playerName);
        teamName.setColor(chatColor);

        return teamName;
    }

}
