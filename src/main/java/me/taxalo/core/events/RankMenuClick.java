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

public class RankMenuClick implements Listener {
    final Core plugin = Core.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        final Player player = (Player) e.getWhoClicked();

        if (!RanksMenu.rankUsers.containsKey(player.getUniqueId())) return;

        final Inventory inventory = RanksMenu.rankUsers.get(player.getUniqueId()).getInventory();

        if (e.getClickedInventory() == null || e.getClickedInventory() != inventory) return;

        e.setCancelled(true);

        val inventoryUser = RanksMenu.getHolder(player.getUniqueId());

        switch (e.getCurrentItem().getType()) {
            case GREEN_WOOL:

                val rankManager = plugin.getRankManager();

                if (!e.getClick().isLeftClick()) return;
                if (e.getCurrentItem().getItemMeta() == null) return;

                val rankName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

                e.getClickedInventory().remove(e.getCurrentItem());

                val userRanks = rankManager.getUser(inventoryUser);

                userRanks.add(rankName);
                rankManager.setUser(inventoryUser, userRanks);

                break;
            case ANVIL:

                final Player inventoryPlayer = Bukkit.getServer().getPlayer(inventoryUser);

                if  (inventoryPlayer == null) {
                    player.closeInventory();
                    return;
                }

                RanksMenu.removeMenu(player.getUniqueId());

                new ManageMenu(inventoryPlayer, player.getUniqueId());

                player.openInventory(ManageMenu.getMenu(player.getUniqueId()));

                break;
        }
    }
}
