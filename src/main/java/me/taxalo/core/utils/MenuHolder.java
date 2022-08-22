package me.taxalo.core.utils;

import lombok.Getter;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class MenuHolder {

    @Getter
    private final Inventory inventory;
    @Getter
    private final UUID creator;

    public MenuHolder(UUID creator, Inventory inventory) {
        this.creator = creator;
        this.inventory = inventory;
    }
}
