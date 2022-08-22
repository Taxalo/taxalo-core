package me.taxalo.core;

import lombok.Getter;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.handlers.CommandHandler;
import me.taxalo.core.handlers.EventHandler;
import me.taxalo.core.managers.ColorManager;
import me.taxalo.core.managers.PermissionManager;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class Core extends JavaPlugin implements Listener {

    @Getter
    public static Core Instance;
    final Settings settings = Settings.getInstance();
    @Getter
    private MongoDB mongoHandler;
    @Getter
    private ColorManager colorManager;
    @Getter
    private RankManager rankManager;
    @Getter
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        Instance = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        new CommandHandler();
        new EventHandler();
        String mongoURI = getConfig().getString("mongoURI");
        if (mongoURI == null || mongoURI.length() == 0) {
            settings.sendMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + " No valid MongoURI was found in config.yml");
            settings.sendMessage(ChatColor.RED + " Please specify a MongoURI in config.yml");
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.shutdown();
        }
        mongoHandler = new MongoDB(mongoURI);
        colorManager = new ColorManager(this);
        permissionManager = new PermissionManager(this);
        rankManager = new RankManager(this);
        settings.sendMessage("Enabled");
        colorManager.loadColors();
    }

    @Override
    public void onDisable() {
        settings.sendMessage("Disabled");
    }
}
