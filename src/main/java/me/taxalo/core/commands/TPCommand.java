package me.taxalo.core.commands;

import me.taxalo.core.utils.MM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            switch (args.length) {
                case 0:
                    sender.sendMessage(MM.getPrefix() + " Specify a player to teleport to.");
                    break;
                case 1:
                    Player player = Bukkit.getServer().getPlayer(args[0]);

                    if (player == null) {
                        sender.sendMessage(MM.getPrefix() + " That is not a valid player");
                        return false;
                    }

                    ((Player) sender).teleport(player.getLocation());

                    sender.sendMessage(MM.getPrefix() + " Teleported to " + player.getName());
                    break;
                case 2:
                    Player firstPlayer = Bukkit.getServer().getPlayer(args[0]);
                    Player secondPlayer = Bukkit.getServer().getPlayer(args[1]);

                    if (firstPlayer == null || secondPlayer == null) {
                        sender.sendMessage(MM.getPrefix() + " One of those players is not valid");
                        return false;
                    }

                    firstPlayer.teleport(secondPlayer.getLocation());
                    firstPlayer.sendMessage(MM.getPrefix() + " Teleported to " + secondPlayer.getName());

                    break;
                case 3:
                    String coordX = args[0];
                    String coordY = args[1];
                    String coordZ = args[2];

                    Player senderPlayer = (Player) sender;

                    senderPlayer.teleport(new Location(senderPlayer.getWorld(), Double.parseDouble(coordX), Double.parseDouble(coordY), Double.parseDouble(coordZ)));
                    senderPlayer.sendMessage(MM.getPrefix() + " Teleported to " + coordX + " " + coordY + " " + coordZ);
                    break;
            }
        } else {
            MM.sendMessage("You need to be a player to execute that command.");
        }
        return false;
    }
}
