package me.taxalo.core.managers;

import me.taxalo.core.Core;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class BackManager {

    Core plugin;
    private final HashMap<UUID, Location> usersLocation;

    public BackManager(Core core) {
        usersLocation = new HashMap<>();
        plugin = core;
    }

    public Boolean isLoaded(UUID uuid) {
        Location location = usersLocation.get(uuid);
        return location != null;
    }

    public Location getLocation(UUID uuid) {
        return usersLocation.get(uuid);
    }

    public void setLocation(UUID uuid, Location location) {
        usersLocation.put(uuid, location);
    }
}
