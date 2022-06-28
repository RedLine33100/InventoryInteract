package fr.redline.invinteract.event;

import fr.redline.invinteract.inv.holder.InventoryInfoHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

public class CloseInventory implements Listener {

    public CloseInventory(JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(this, javaPlugin);
    }

    public void closedInventory(Player player, InventoryInfoHolder invHolder) {
        invHolder.getInventoryCreator().onCloseInventory(player, invHolder);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player))
            return;
        if (!(event.getInventory().getHolder() instanceof InventoryInfoHolder))
            return;

        InventoryInfoHolder inventoryCreator = (InventoryInfoHolder) event.getInventory().getHolder();
        closedInventory((Player) event.getPlayer(), inventoryCreator);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        InventoryView invView = event.getPlayer().getOpenInventory();
        if (invView == null)
            return;

        Inventory topInventory = invView.getTopInventory();
        if (topInventory == null)
            return;

        if (topInventory.getHolder() instanceof InventoryInfoHolder)
            closedInventory(event.getPlayer(), (InventoryInfoHolder) topInventory.getHolder());

    }

}
