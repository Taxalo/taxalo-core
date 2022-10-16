package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.BackManager;
import me.taxalo.core.utils.MM;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        final BackManager backManager = Core.getInstance().getBackManager();
        if (backManager == null) return true;

        final Player player = (Player) sender;
        if (!backManager.isLoaded(player.getUniqueId())) return false;

        final Location location = backManager.getLocation(player.getUniqueId());

        player.teleport(location);
        player.sendMessage(MM.getPrefix() + "You have been teleported to your last death location.");

        return false;
    }
}
