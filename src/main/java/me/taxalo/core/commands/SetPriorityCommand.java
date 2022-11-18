package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.MM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPriorityCommand implements CommandExecutor {

    Core plugin = Core.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        RankManager rankManager = plugin.getRankManager();

        switch (args.length) {
            case 0:
            case 1:
                sender.sendMessage(MM.getPrefix() + " Correct usage: /setpriority <rank> <priority>.");
                return false;
            case 2:

                if (!rankManager.rankExists(args[0])) {
                    sender.sendMessage(MM.getPrefix() + " Rank " + args[0] + " does not exist.");
                    return false;
                }

                rankManager.setPriority(args[0], Integer.parseInt(args[1]));

                sender.sendMessage(MM.getPrefix() + " Set priority of " + args[0] + " to " + args[1]);
        }
        return false;
    }
}
