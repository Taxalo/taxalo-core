package me.taxalo.core.events;

import lombok.val;
import me.taxalo.core.Core;
import me.taxalo.core.inventories.ManageMenu;
import me.taxalo.core.inventories.RanksMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ManageMenuClick implements Listener {
    final Core plugin = Core.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        Player player = (Player) e.getWhoClicked();
        if (!ManageMenu.managedUsers.containsKey(player.getUniqueId())) return;

        Inventory inventory = ManageMenu.managedUsers.get(player.getUniqueId()).getInventory();
        if (e.getClickedInventory() == null || e.getClickedInventory() != inventory) return;
        e.setCancelled(true);

        val inventoryUser = ManageMenu.getHolder(player.getUniqueId());
        val rankManager = plugin.getRankManager();

        switch (e.getCurrentItem().getType()) {
            case PAPER:
                if (!e.getClick().isRightClick()) return;
                if (e.getCurrentItem().getItemMeta() == null) return;
                val rankName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                e.getClickedInventory().remove(e.getCurrentItem());
                val userRanks = rankManager.getUser(inventoryUser);
                userRanks.remove(rankName);
                rankManager.setUser(inventoryUser, userRanks);
                break;
            case ANVIL:
                Player inventoryPlayer = Bukkit.getServer().getPlayer(inventoryUser);
                if (inventoryPlayer == null) {
                    player.closeInventory();
                    return;
                }
                ManageMenu.removeMenu(player.getUniqueId());
                new RanksMenu(inventoryPlayer, player.getUniqueId());
                player.openInventory(RanksMenu.getMenu(player.getUniqueId()));
                break;
        }
    }
}
