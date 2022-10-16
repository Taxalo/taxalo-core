package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.MM;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {

    final Core plugin = Core.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        final RankManager rankManager = plugin.getRankManager();

        switch (args.length) {
            case 0:
                sender.sendMessage(MM.getPrefix() + " Specify an action (create/remove/list) and a name if neededm");
                break;
            case 1:
                if (!args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(MM.getPrefix() + " Specify a name to create / remove a rank.");
                    return false;
                }

                final String list = String.join(ChatColor.RESET + ", " + ChatColor.GOLD, rankManager.getRanks());
                sender.sendMessage(MM.getPrefix() + " Ranks: " + ChatColor.GOLD + list);

                break;
            case 2:
                switch (args[0]) {
                    case "create":
                        rankManager.addRank(args[1]);
                        sender.sendMessage(MM.getPrefix() + " Created rank " + ChatColor.GOLD + args[1]);
                        break;
                    case "remove":
                        rankManager.removeRank(args[1]);
                        sender.sendMessage(MM.getPrefix() + " Removed rank " + ChatColor.GOLD + args[1]);
                        break;
                }
        }
        return false;
    }
}
