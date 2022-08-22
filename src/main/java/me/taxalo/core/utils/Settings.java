package me.taxalo.core.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Settings {

    @Getter
    public static final Settings Instance;
    @Getter
    final String prefix = ChatColor.WHITE.asBungee() + "[" + ChatColor.GOLD + "TaxaloCore" + ChatColor.WHITE + "]";

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + " " + message);
    }
    static {
        Instance = new Settings();
    }
}
