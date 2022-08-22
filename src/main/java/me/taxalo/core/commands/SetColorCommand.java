package me.taxalo.core.commands;

import me.taxalo.core.Core;
import me.taxalo.core.managers.ColorManager;
import me.taxalo.core.utils.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SetColorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player senderPlayer = (Player) sender;
        ColorManager colorManager = Core.getInstance().getColorManager();
        switch (args.length) {
            case 0:
                sender.sendMessage(Settings.getInstance().getPrefix() + " Especifíca un color.");
                break;
            case 1:
                UUID playerUUID = senderPlayer.getUniqueId();
                switch (args[0].toLowerCase()) {
                    case "darkred":
                        colorManager.setColor(playerUUID, ChatColor.DARK_RED.toString());
                        break;
                    case "red":
                        colorManager.setColor(playerUUID, ChatColor.RED.toString());
                        break;
                    case "gold":
                        colorManager.setColor(playerUUID, ChatColor.GOLD.toString());
                        break;
                    case "yellow":
                        colorManager.setColor(playerUUID, ChatColor.YELLOW.toString());
                        break;
                    case "darkgreen":
                        colorManager.setColor(playerUUID, ChatColor.DARK_GREEN.toString());
                        break;
                    case "green":
                        colorManager.setColor(playerUUID, ChatColor.GREEN.toString());
                        break;
                    case "aqua":
                        colorManager.setColor(playerUUID, ChatColor.AQUA.toString());
                        break;
                    case "darkaqua":
                        colorManager.setColor(playerUUID, ChatColor.DARK_AQUA.toString());
                        break;
                    case "darkblue":
                        colorManager.setColor(playerUUID, ChatColor.DARK_BLUE.toString());
                        break;
                    case "blue":
                        colorManager.setColor(playerUUID, ChatColor.BLUE.toString());
                        break;
                    case "lightpurple":
                        colorManager.setColor(playerUUID, ChatColor.LIGHT_PURPLE.toString());
                        break;
                    case "darkpurple":
                        colorManager.setColor(playerUUID, ChatColor.DARK_PURPLE.toString());
                        break;
                    case "white":
                        colorManager.setColor(playerUUID, ChatColor.WHITE.toString());
                        break;
                    case "gray":
                        colorManager.setColor(playerUUID, ChatColor.GRAY.toString());
                        break;
                    case "darkgray":
                        colorManager.setColor(playerUUID, ChatColor.DARK_GRAY.toString());
                        break;
                    case "black":
                        colorManager.setColor(playerUUID, ChatColor.BLACK.toString());
                        break;
                    case "list":
                        sender.sendMessage(Settings.getInstance().getPrefix() + " DARKRED, RED, GOLD, YELLOW, DARKGREEN, GREEN, AQUA, DARKAQUA, DARKBLUE, BLUE, LIGHTPURPLE, DARKPURPLE, WHITE, GRAY, DARKGRAY, BLACK.");
                        return false;
                    default:
                        sender.sendMessage(Settings.getInstance().getPrefix() + " Pick a color from /setcolor list");
                        return false;
                }
                sender.sendMessage(Settings.getInstance().getPrefix() + " Your name will be displayed as: " + colorManager.getColor(playerUUID) + sender.getName() + ChatColor.RESET + ".");
                break;
        }
        return false;
    }
}
