package me.taxalo.core.inventories;

import dev.dbassett.skullcreator.SkullCreator;
import lombok.Getter;
import lombok.val;
import me.taxalo.core.Core;
import me.taxalo.core.managers.ColorManager;
import me.taxalo.core.utils.ItemCreator;
import me.taxalo.core.utils.MenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class ManageMenu {

    Core plugin = Core.getInstance();

    @Getter
    private static final String inventoryName = ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Managing Menu";
    ItemStack rankManager = ItemCreator.createGuiItem(Material.ANVIL, ChatColor.GOLD + "Manage ranks", ChatColor.GRAY + "Opens a GUI to add available ranks");
    public static HashMap<UUID, MenuHolder> managedUsers = new HashMap<>();


    public ManageMenu(Player player, UUID creatorUUID) {

        ColorManager colorManager = plugin.getColorManager();
        Inventory inventory = Bukkit.createInventory(null, 54, inventoryName);

        val backgroundItem = ItemCreator.createGuiItem(Material.GREEN_STAINED_GLASS_PANE, ChatColor.RESET.toString());

        Arrays.asList(
                        1, 2, 3, 4, 5, 6, 7, 8,
                        9, 18, 27, 36, 45,
                        17, 26, 35, 44,
                        46, 47, 48, 49, 50, 51, 52)
                .forEach(integer -> inventory.setItem(integer, backgroundItem));

        ItemStack playerHead = SkullCreator.itemFromUuid(player.getUniqueId());
        ItemMeta headMeta = playerHead.getItemMeta();

        if (headMeta != null) {
            headMeta.setDisplayName(colorManager.getColor(player.getUniqueId()) + player.getName());
            playerHead.setItemMeta(headMeta);
        }

        inventory.setItem(0, playerHead);

        val userRanks = plugin.getRankManager().getUser(player.getUniqueId());

        if (userRanks != null) {
            for (String rank : userRanks) {
                val rankItem = ItemCreator.createGuiItem(Material.PAPER, ChatColor.AQUA + rank, "", ChatColor.RED + ChatColor.UNDERLINE.toString() + "RIGHT CLICK" + ChatColor.RED + " to remove the rank");
                inventory.addItem(rankItem);
            }
        }

        inventory.setItem(53, rankManager);

        MenuHolder menuHolder = new MenuHolder(player.getUniqueId(), inventory);
        managedUsers.put(creatorUUID, menuHolder);
    }

    public static Inventory getMenu(UUID creatorUUID) {
        MenuHolder menuHolder = managedUsers.get(creatorUUID);
        return menuHolder.getInventory();
    }

    public static void removeMenu(UUID creatorUUID) {
        managedUsers.remove(creatorUUID);
    }

    public static UUID getHolder(UUID creatorUUID) {
        MenuHolder menuHolder = managedUsers.get(creatorUUID);
        return menuHolder.getCreator();
    }

}
