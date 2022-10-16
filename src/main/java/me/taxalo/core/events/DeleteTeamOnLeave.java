package me.taxalo.core.events;

import me.taxalo.core.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class DeleteTeamOnLeave implements Listener {

    final Core plugin = Core.getInstance();

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        final Scoreboard scoreboard = plugin.getScoreboard();

        final Team team = scoreboard.getTeam(e.getPlayer().getName());
        if (team == null) return;

        team.unregister();
    }

}
