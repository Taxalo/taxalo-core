package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermsCommand implements CommandExecutor {
    // /perms [add/remove/list] [rank] [permission]
    final Core plugin = Core.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        RankManager rankManager = plugin.getRankManager();

        switch (args.length) {
            case 0:
                sender.sendMessage(Settings.getInstance().getPrefix() + " Specify an action (add/remove/list) and the rank name.");
                break;
            case 2:
                if (!args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(Settings.getInstance().getPrefix() + " Specify the rank name.");
                    return false;
                }

                if (!rankManager.rankExists(args[1])) {
                    sender.sendMessage(Settings.getInstance().getPrefix() + " Specify a valid rank");
                    return false;
                }

                String list = String.join(ChatColor.RESET + ", " + ChatColor.GOLD, rankManager.getPermissions(args[1]));
                sender.sendMessage(Settings.getInstance().getPrefix() + " Permissions of " + ChatColor.GOLD + args[1] + ChatColor.RESET + ": " + ChatColor.GOLD + list);

                break;
            case 3:
                switch (args[0]) {
                    case "add":
                        if (!rankManager.rankExists(args[1])) {
                            sender.sendMessage(Settings.getInstance().getPrefix() + " Specify a valid rank");
                            return false;
                        }
                        rankManager.addPermission(args[1], args[2]);
                        sender.sendMessage(Settings.getInstance().getPrefix() + " Added permission to " + ChatColor.GOLD + args[1] + ChatColor.RESET + ": " + ChatColor.GOLD + args[2]);
                        break;
                    case "remove":
                        if (!rankManager.rankExists(args[1])) {
                            sender.sendMessage(Settings.getInstance().getPrefix() + " Specify a valid rank");
                            return false;
                        }

                        rankManager.removePermission(args[1], args[2]);
                        sender.sendMessage(Settings.getInstance().getPrefix() + " Removed permission from " + ChatColor.GOLD + args[1] + ChatColor.RESET + ": " + ChatColor.GOLD + args[2]);
                        break;
                }
        }
        return false;
    }
}
