package me.taxalo.core.handlers;

import me.taxalo.core.Core;
import me.taxalo.core.commands.*;
import me.taxalo.core.utils.Settings;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Objects;

public class CommandHandler {
    public CommandHandler() {
        Arrays.asList(
            new ManageCommand(), new SetColorCommand(), new RankCommand(), new PermsCommand(), new TPCommand()
        ).forEach(command -> {
            String commandName = command.getClass().getSimpleName().toLowerCase().split("command")[0];
            Objects.requireNonNull(Core.getInstance().getCommand(commandName)).setExecutor(command);
            Settings.getInstance().sendMessage("Loaded command " + ChatColor.GOLD + commandName);
        });
    }
}
