package me.taxalo.core.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MM {

    @Getter
    public static String prefix = ChatColor.WHITE.asBungee() + "[" + ChatColor.GOLD + "TaxaloCore" + ChatColor.WHITE + "]";

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    public static void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + " " + message);
    }
}
