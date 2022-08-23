package me.taxalo.core.handlers;

import me.taxalo.core.Core;
import me.taxalo.core.events.*;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class EventHandler {
    public EventHandler() {
        Arrays.asList(
                new LoadColorsOnJoin(),new LoadRankOnJoin(), new RemoveRankOnLeave(),
                new MessageSend(),
                new ManageMenuClick(), new RankMenuClick(),
                new SaveDeathLocation()
        ).forEach(event -> Bukkit.getServer().getPluginManager().registerEvents(event, Core.getInstance()));
    }
}
