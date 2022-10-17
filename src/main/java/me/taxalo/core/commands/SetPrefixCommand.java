package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.MM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPrefixCommand implements CommandExecutor {

    final Core plugin = Core.getInstance();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        final RankManager rankManager = plugin.getRankManager();

        switch (args.length) {
            case 0:
            case 1:
                sender.sendMessage(MM.getPrefix() + " Correct usage: /setprefix <rank> <prefix>.");
                return false;
            case 2:
                if (args[1].length() > 16) {
                    sender.sendMessage(MM.getPrefix() + " Prefixes can only be 16 characters long.");
                    return false;
                }

                if (!rankManager.rankExists(args[0])) {
                    sender.sendMessage(MM.getPrefix() + " Rank " + args[0] + " does not exist.");
                    return false;
                }

                rankManager.setPrefix(args[0], args[1]);
        }
        return false;
    }
}
