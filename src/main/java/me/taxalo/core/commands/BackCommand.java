package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.BackManager;
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


        BackManager backManager = Core.getInstance().getBackManager();
        if (backManager == null) return true;

        Player player = (Player) sender;
        if (!backManager.isLoaded(player.getUniqueId())) return false;

        Location location = backManager.getLocation(player.getUniqueId());
        player.teleport(location);
        return false;
    }
}
