package me.taxalo.core.commands;

import lombok.val;
import me.taxalo.core.inventories.ManageMenu;
import me.taxalo.core.utils.MM;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        switch (args.length) {
            case 0:
                sender.sendMessage(MM.getPrefix() + " Please specify an user to manage.");
                break;
            case 1:
                val senderPlayer = (Player) sender;
                val player = Bukkit.getServer().getPlayer(args[0]);
                if (player == null) return false;
                new ManageMenu(player, senderPlayer.getUniqueId());

                senderPlayer.openInventory(ManageMenu.getMenu(senderPlayer.getUniqueId()));

                break;
        }

        return false;
    }
}
