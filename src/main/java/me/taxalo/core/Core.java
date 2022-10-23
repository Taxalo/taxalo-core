package me.taxalo.core;

import lombok.Getter;
import me.taxalo.core.database.MongoDB;
import me.taxalo.core.handlers.CommandHandler;
import me.taxalo.core.handlers.EventHandler;
import me.taxalo.core.managers.BackManager;
import me.taxalo.core.managers.ColorManager;
import me.taxalo.core.managers.PermissionManager;
import me.taxalo.core.managers.RankManager;
import me.taxalo.core.utils.MM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

@Getter
public final class Core extends JavaPlugin implements Listener {

    @Getter
    public static Core Instance;
    private MongoDB mongoHandler;
    private ColorManager colorManager;
    private RankManager rankManager;
    private PermissionManager permissionManager;
    private BackManager backManager;
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        Instance = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        new CommandHandler();
        new EventHandler();

        final String mongoURI = getConfig().getString("mongoURI");

        if (mongoURI == null || mongoURI.length() == 0) {
            MM.sendMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + " No valid MongoURI was found in config.yml");
            MM.sendMessage(ChatColor.RED + " Please specify a MongoURI in config.yml");

            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.shutdown();
        }

        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

        mongoHandler = new MongoDB(mongoURI);
        colorManager = new ColorManager(this);
        permissionManager = new PermissionManager(this);
        rankManager = new RankManager(this);
        backManager = new BackManager(this);

        colorManager.loadColors();

        MM.sendMessage("Enabled");
    }

    @Override
    public void onDisable() {
        MM.sendMessage("Disabled");
    }
}
